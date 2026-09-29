package net.codesup.util.emit

import net.codesup.util.emit.FilesystemOutputContext
import net.codesup.util.emit.SourceBuilder
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Compiles all files in one model together, without relying on a locally installed kotlinc. */
internal object CompilationHarness {
    data class Result(val exitCode: ExitCode, val diagnostics: String, val sources: Map<String, String>, val classes: List<String>) {
        fun assertCompiles() {
            assertEquals(ExitCode.OK, exitCode, report())
            assertTrue(classes.isNotEmpty(), "Compiler produced no class files.\n${report()}")
        }
        fun assertFails() {
            assertEquals(ExitCode.COMPILATION_ERROR, exitCode, report())
            assertTrue(classes.isNotEmpty(), "Compiler produced no class files.\n${report()}")
        }

        fun report(): String = buildString {
            appendLine(diagnostics)
            sources.forEach { (name, source) ->
                appendLine("--- $name ---")
                source.lineSequence().forEachIndexed { index, line -> appendLine("${index + 1}: $line") }
            }
        }
    }

    fun compile(model: SourceBuilder): Result {
        val directory = Files.createDirectories(Paths.get("build/generated-tests/")).toFile()
        try {
            val sourcesDirectory = File(directory, "sources")
            model.generate(FilesystemOutputContext(sourcesDirectory.toPath()))
            val files = sourcesDirectory.walkTopDown().filter { it.isFile && it.extension == "kt" }.sortedBy { it.path }.toList()
            require(files.isNotEmpty()) { "The model generated no Kotlin source files" }
            val output = File(directory, "classes")
            val messages = ByteArrayOutputStream()
            // Only expose the standard library to generated code, not the test fixtures.
            val stdlib = File(Unit::class.java.protectionDomain.codeSource.location.toURI())
            val exitCode = PrintStream(messages, true, Charsets.UTF_8).use { stream ->
                K2JVMCompiler().exec(stream, "-no-stdlib", "-no-reflect", "-classpath", stdlib.path,
                    "-d", output.path, *files.map { it.path }.toTypedArray())
            }
            return Result(exitCode, messages.toString(Charsets.UTF_8),
                files.associate { it.relativeTo(sourcesDirectory).invariantSeparatorsPath to it.readText() },
                output.walkTopDown().filter { it.isFile && it.extension == "class" }
                    .map { it.relativeTo(output).invariantSeparatorsPath }.toList())
        } finally {
            directory.deleteRecursively()
        }
    }
}
