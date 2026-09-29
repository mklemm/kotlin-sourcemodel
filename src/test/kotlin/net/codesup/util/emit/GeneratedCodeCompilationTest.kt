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
