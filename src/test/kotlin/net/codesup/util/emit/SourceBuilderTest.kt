package net.codesup.util.emit

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.sourceBuilder
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.temporal.Temporal
import kotlin.test.Test

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class SourceBuilderTest {
    @Test
    fun testBuildNonsense() {
        sourceBuilder {
            val myPkg = "net.codesup.util"
            val myReturnClass = externalType( "$myPkg.AnotherClass")
            _package(myPkg) {
                _file("TestClass") {
                    _class("TestClass") {
                        typeParam("T") {
                            bound {
                                receiver(Any::class) {
                                    isNullable = true
                                }
                                type(myReturnClass) {
                                    arg("T")
                                }
                            }
                        }
                        primaryConstructor {
                            property("firstParam") {
                                isMutable = false
                                type(myReturnClass)
                            }
                        }
                        _property("var") {
                            isMutable = true
                            type(stringType)
                        }
                        _property("property2") {
                            isMutable = false
                            type(QualifiedName("AnotherClass")) {
                                arg("T")
                            }
                            init {
                                (v("x") - 1) * v("y")
                            }
                        }
                    }
                }
            }
        }.generate(OutputContext(Paths.get("build/generated-sources/test")))
    }

    @Test
    fun testBuildReal() {
        sourceBuilder {
            val myPkg = "net.codesup.util"
            val myReturnClass = externalType( LocalDateTime::class)
            _package(myPkg) {
                _file("TestDateTimeClass") {
                    _class("TestDateTimeClass") {
                        typeParam("T") {
                            bound(Temporal::class)
                        }
                        primaryConstructor {
                            property("temporalProperty") {
                                isMutable = false
                                type(myReturnClass)
                            }
                        }
                        _property("var") {
                            isMutable = true
                            type(stringType)
                            init {
                                str("Initial Value")
                            }
                        }
                        _property("property2") {
                            isMutable = false
                            type(this@_class) {
                                arg(myReturnClass)
                            }
                            init {
                                call(myReturnClass)
                            }
                        }
                    }
                }
            }
        }.generate(OutputContext(Paths.get("build/generated-sources/test")))
    }
}
