package net.codesup.util.emit

import net.codesup.util.emit.declaration.DeclarationOwner
import net.codesup.util.emit.declaration.DeclarationScope
import net.codesup.util.emit.declaration.PackageDeclaration
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

val reservedWords = listOf("class", "interface", "package", "var", "val", "fun", "return", "this")

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
abstract class OutputContext() {
    var currentWriter: BufferedWriter? = null
    private var indent: Int = 0
    private val indentChars = "\t"

    val importedSymbolReverseMap = mutableMapOf<QualifiedName, QualifiedName>()

    abstract fun file(packageName: String, baseName: String, block: OutputContext.() -> Unit)

    fun file(packageDeclaration: PackageDeclaration, baseName: String, block: OutputContext.() -> Unit) {
        file(packageDeclaration.toString(), baseName, block)
    }

    fun w(qualifiedName: QualifiedName): OutputContext {
        w((importedSymbolReverseMap[qualifiedName] ?: qualifiedName).quotedStringValue)
        return this
    }

    fun w(s: String): OutputContext {
        currentWriter?.write(s)
        return this
    }

    fun g(scope: DeclarationOwner, obj: Generable?): OutputContext {
        obj?.generate(scope, this)
        return this
    }

    fun q(s: String): OutputContext = w(quote(s))

    fun wl(s: String = ""): OutputContext {
        w(s)
        w(System.lineSeparator())
        return this
    }

    fun wi(): OutputContext {
        w(indentChars.repeat(indent))
        return this
    }

    fun increaseIndent(): Int = indent++
    fun decreaseIndent(): Int = --indent

    fun <T : Generable> list(scope: DeclarationOwner,
        list: List<T>,
        separator: String = ", ",
        prefix: String = "",
        suffix: String = ""
    ): OutputContext {
        if (list.isNotEmpty()) {
            w(prefix)
            list.forEachIndexed { index, t ->
                if (index > 0) {
                    w(separator)
                }
                t.generate(scope, this)
            }
            w(suffix)
        }
        return this
    }

    fun join(list: List<String>, separator: String = ", ", prefix: String = "", suffix: String = ""): OutputContext {
        if (list.isNotEmpty()) {
            w(prefix)
            list.forEachIndexed { index, t ->
                if (index > 0) {
                    w(separator)
                }
                w(t)
            }
            w(suffix)
        }
        return this
    }
}

class FilesystemOutputContext(val target: Path): OutputContext() {
    override fun file(packageName: String, baseName: String, block: OutputContext.() -> Unit) {
        val relativePath = Paths.get(packageName.replace('.','/')).resolve("${baseName}.kt")
        val targetPath = relativePath?.let { target.resolve(it) } ?: target
        Files.createDirectories(targetPath.parent)
        currentWriter = Files.newBufferedWriter(targetPath)
        importedSymbolReverseMap.clear()
        block()
        currentWriter?.close()
        currentWriter = null
    }
}

fun quote(name: String): String = if (name in reservedWords || (name.firstOrNull()?.let { f -> f !in 'A'..'Z' && f !in 'a'..'z' && f != '_'} == true )) "`$name`" else name
