package net.codesup.util.emit

import net.codesup.util.emit.sourceBuilder
import net.codesup.util.emit.declaration.ClassDeclaration
import org.jetbrains.kotlin.cli.common.ExitCode
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratedCodeCompilationTest {
    @Test
    fun constructorPropertiesAndReservedNames() {
        CompilationHarness.compile(sourceBuilder {
            _package("generated.properties") {
                _file("Properties") {
                    _class("Properties") {
                        primaryConstructor {
                            _val("class") { type(String::class) }
                            _var("count") { type(Int::class) }
                        }
                        _var("var") { type(String::class); init { str("initial") } }
                        _val("optional") { type(String::class) { isNullable = true }; init { _null() } }
                    }
                }
            }
        }).assertCompiles()
    }

    @Test
    fun genericClassAndCrossPackageReferences() {
        CompilationHarness.compile(sourceBuilder {
            lateinit var box: ClassDeclaration
            _package("generated.model") {
                _file("Box") {
                    box = _class("Box") {
                        typeParam("T") { bound(Any::class) }
                        primaryConstructor { _val("value") { type("T") } }
                    }
                }
            }
            _package("generated.consumer") {
                _file("Consumer") {
                    _class("Consumer") {
                        primaryConstructor {
                            _val("box") { type(box) { arg(String::class) } }
                            _val("dates") { type(List::class) { arg(LocalDateTime::class) } }
                        }
                    }
                }
            }
        }).assertCompiles()
    }

    @Test
    fun samePackageReferencesAcrossFilesUseShortNamesWithoutImports() {
        val result = CompilationHarness.compile(sourceBuilder {
            _package("generated.shared") {
                lateinit var item: ClassDeclaration
                _file("Item") { item = _class("Item") }
                _file("Other") { _class("Other") }
                _file("Consumer") {
                    _class("Consumer") {
                        primaryConstructor {
                            _val("direct") { type(item) }
                            _val("external") { type(externalType("generated.shared.Other")) }
                        }
                    }
                }
            }
        })
        result.assertCompiles()
        val source = result.sources.getValue("generated/shared/Consumer.kt")
        assertTrue(source.contains("direct: Item"), source)
        assertTrue(source.contains("external: Other"), source)
        assertTrue(source.lineSequence().none { it.startsWith("import ") }, source)
    }

    @Test
    fun parentPackageStillRequiresAnImport() {
        val result = CompilationHarness.compile(sourceBuilder {
            _package("generated") { _file("Parent") { _class("Parent") } }
            _package("generated.child") {
                _file("Consumer") {
                    _class("Consumer") {
                        primaryConstructor {
                            _val("parent") { type(externalType("generated.Parent")) }
                        }
                    }
                }
            }
        })
        result.assertCompiles()
        val source = result.sources.getValue("generated/child/Consumer.kt")
        assertTrue(source.contains("import generated.Parent"), source)
        assertTrue(source.contains("parent: Parent"), source)
    }

    @Test
    fun conflictingImportedTypeNames() {
        val result = CompilationHarness.compile(sourceBuilder {
            _package("generated.clashes") {
                _file("Dates") {
                    _class("Dates") {
                        primaryConstructor {
                            _val("utilDate") { type(java.util.Date::class) }
                            _val("sqlDate") { type(java.sql.Date::class) }
                        }
                    }
                }
            }
        })
        result.assertCompiles()
    }

    @Test
    fun localDeclarationsArithmeticAndExternalCalls() {
        CompilationHarness.compile(sourceBuilder {
            val println = externalFunction("kotlin.io.println")
            _package("generated.functions") {
                _file("Functions") {
                    _fun("calculate") {
                        val input = param("input") { type(typeUse(Int::class)) }
                        block {
                            _val("result") { init { (v(input) - 1) * 2 } }
                            st {
                                val result = v("result")
                                call(println) { arg(result) }
                            }
                        }
                    }
                    _fun("extension") {
                        receiver("kotlin.String") {}
                        block {
                            st {
                                val self = _this()
                                call(println) { arg(self) }
                            }
                        }
                    }
                }
            }
        }).assertCompiles()
    }

    @Test
    fun nestedClassCompanionAndFunctionTypedParameter() {
        CompilationHarness.compile(sourceBuilder {
            _package("generated.nested") {
                _file("Container") {
                    _class("Container") {
                        _class("Nested")
                        _companion {
                            _val("label") { init { str("container") } }
                        }
                        _fun("accept") {
                            param("callback") { type { receiver(String::class); type(typeUse(Unit::class)) } }
                            block {}
                        }
                    }
                }
            }
        }).assertCompiles()
    }

    @Test
    fun defaultJvmImportsUseShortNamesWithoutImportDirectives() {
        val result = CompilationHarness.compile(sourceBuilder {
            _package("generated.defaults") {
                _file("Defaults") {
                    _class("Defaults") {
                        primaryConstructor {
                            _val("text") { type(String::class) }
                            _val("items") { type(List::class) { arg(String::class) } }
                            _val("thread") { type(Thread::class) }
                            _val("annotation") { type(kotlin.jvm.JvmName::class) }
                            _val("date") { type(LocalDateTime::class) }
                            _val("type") { type(kotlin.reflect.KClass::class) { arg(String::class) } }
                        }
                    }
                    _fun("printMessage") {
                        block {
                            st {
                                val message = str("hello")
                                call(externalFunction("kotlin.io.println")) { arg(message) }
                            }
                        }
                    }
                }
            }
        })
        result.assertCompiles()
        val source = result.sources.values.single()
        assertEquals(listOf("import kotlin.reflect.KClass", "import java.time.LocalDateTime"),
            source.lineSequence().filter { it.startsWith("import ") }.toList(), source)
        for (reference in listOf("text: String", "items: List<String>", "thread: Thread",
            "annotation: JvmName", "println(\"hello\")")) {
            assertTrue(source.contains(reference), source)
        }
    }

    @Test
    fun defaultImportedTypeRemainsQualifiedWhenItsNameClashes() {
        val result = CompilationHarness.compile(sourceBuilder {
            _package("generated.defaults") {
                _file("String") {
                    _class("String") {
                        primaryConstructor {
                            _val("text") { type(String::class) }
                        }
                    }
                }
            }
        })
        result.assertCompiles()
        val source = result.sources.values.single()
        assertTrue(source.contains("text: kotlin.String"), source)
        assertTrue(source.lineSequence().none { it.startsWith("import ") }, source)
    }

    @Test
    fun harnessRejectsInvalidGeneratedCodeWithDiagnostics() {
        val result = CompilationHarness.compile(sourceBuilder {
            _package("generated.invalid") {
                _file("Invalid") {
                    _val("number") { type(Int::class); init { str("not an integer") } }
                }
            }
        })
        assertEquals(ExitCode.COMPILATION_ERROR, result.exitCode, result.report())
        assertTrue(result.diagnostics.contains("Invalid.kt"), result.report())
        assertTrue(result.report().contains("not an integer"))
    }
}
