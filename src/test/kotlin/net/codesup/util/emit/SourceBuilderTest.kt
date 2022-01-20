package net.codesup.util.emit

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.sourceBuilder
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse
import org.junit.jupiter.api.Test
import java.nio.file.Paths

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class SourceBuilderTest {
    @Test
    fun testBuild() {
        sourceBuilder {
            val myPkg = "net.codesup.util"
            val myReturnClass = ClassTypeUse(myPkg, "AnotherClass")
            _package(myPkg) {
                _file("TestClass") {
                    _class("TestClass") {
                        typeParam("T") {
                            bound {
                                receiver(KClassUse.any) {
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
                            type(KClassUse.string)
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

}
