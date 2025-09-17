package net.codesup.util.emit

import net.codesup.emit.OutputContext
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.functionName
import net.codesup.emit.qualifiedName
import net.codesup.emit.sourceBuilder
import net.codesup.util.stream.testFunction6
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.temporal.Temporal
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class SourceBuilderTest {
    @Test
    fun testExample1() {
        sourceBuilder {
            val myReturnClass = externalType("com.example.MyExternalClass") // reference a class by name
            _package("net.codesup.util") {
                _file("TestClass") {
                    val myClass = _class("TestClass") { // create a class and capture it for further use
                        typeParam("T") { // create a type parameter with bounds
                            bound(Any::class) {
                                isNullable = true
                            }
                        }
                        primaryConstructor { // create a primary constructor with parameter property
                            _val("firstParam") {
                                type(String::class) // reference an existing class on the classpath
                            }
                        }
                        _var("var") {
                            type(myReturnClass)
                        }
                        _val("property2") { // create a property with type argument
                            // and initializing expression
                            type(myReturnClass) {
                                arg("T") // set a type argument
                            }
                            init {
                                (v("x") - 1) * v("y") // specify an initializing expression, "v" creates a variable
                            }
                        }
                        _fun("myFunction") {
                            typeParam("F") {
                                bound(Any::class)
                            }
                            val myParam = param("myParameter") {
                                type { // Generate a function type inline
                                    receiver(this@_class) {
                                        arg(LocalDateTime::class)
                                    }
                                }
                            }
                            block { // generate the function block
                                st { // generate a separate statement in the block
                                    myParam.call(this@_fun, v(myParam)) // generate a recursive function call
                                }
                            }
                        }
                    }
                }
            }
        }.generate(OutputContext(Paths.get("build/generated-sources/test")))

    }

    @Test
    fun testExample2() {
        sourceBuilder {
            val myReturnClass = externalType("com.example.MyExternalClass") // reference a class by name
            _package("net.codesup.util") {
                    val myClass = _class("TestClass2") { // create a class and capture it for further use
                        typeParam("T") { // create a type parameter with bounds
                            bound(Any::class) {
                                isNullable = true
                            }
                        }
                        primaryConstructor { // create a primary constructor with parameter property
                            _val("firstParam") {
                                type(String::class) // reference an existing class on the classpath
                            }
                        }
                        _var("var") {
                            type(myReturnClass)
                        }
                        _val("property2") { // create a property with type argument
                            // and initializing expression
                            type(myReturnClass) {
                                arg("T") // set a type argument
                            }
                            init {
                                (v("x") - 1) * v("y") // specify an initializing expression, "v" creates a variable
                            }
                        }
                        _fun("myFunction") {
                            typeParam("F") {
                                bound(Any::class)
                            }
                            val myParam = param("myParameter") {
                                type { // Generate a function type inline
                                    receiver(this@_class) {
                                        arg(LocalDateTime::class)
                                    }
                                }
                            }
                            block { // generate the function block
                                st { // generate a separate statement in the block
                                    myParam.call(this@_fun, v(myParam)) // generate a recursive function call
                                }
                            }
                        }
                    }
            }
        }.generate(OutputContext(Paths.get("build/generated-sources/test")))

    }

    @Test
    fun testBuildNonsense() {
        var testClass: ClassDeclaration? = null
        val sb = sourceBuilder {
            val myPkg = "net.codesup.util"
            val myReturnClass = externalType( "$myPkg.AnotherClass")
            _package(myPkg) {
                _file("TestClass") {
                    testClass = _class("TestClass") {
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
                            type(myReturnClass) {
                                arg("T")
                            }
                            init {
                                (v("x") - 1) * v("y")
                            }
                        }
                    }
                }
            }
        }
        assertEquals("net.codesup.util.TestClass", testClass!!.qualifiedName.toString())
        sb.generate(OutputContext(Paths.get("build/generated-sources/test")))
    }

    @Test
    fun testResolveClashes() {
        var testClass: ClassDeclaration? = null
        val sb = sourceBuilder {
            val myPkg = "net.codesup.util"
            val myReturnClass = externalType( "$myPkg.String")
            _package(myPkg) {
                _file("String") {
                    _class("String") {
                        primaryConstructor {
                            _val("text") {
                                type(stringType)
                            }
                        }
                    }
                }
                _file("StringTestClass") {
                    testClass = _class("StringTestClass") {
                        primaryConstructor {
                            _val("firstParam") {
                                type(myReturnClass)
                            }
                        }
                        _var("var") {
                            type(stringType)
                            init {
                                str("text")
                            }
                        }
                    }
                }
            }
        }.generate(OutputContext(Paths.get("src/test/kotlin")))
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
                            type(List::class) {
                                isNullable = true
                                arg(myReturnClass)
                            }
                            init {
                                call(externalFunction("kotlin.collections.listOf")) {
                                    arg(myReturnClass.fn("now"))
                                }
                            }
                        }
                    }
                }
            }
        }.generate(OutputContext(Paths.get("src/test/kotlin")))
    }

    @Test
    fun testGetFunctionQualifiedName() {
        assertEquals(functionName("net.codesup.util.emit.testFunction1"), ::testFunction1.qualifiedName)
        assertEquals(functionName("net.codesup.util.emit.TestClass.testFunction2"), TestClass::testFunction2.qualifiedName)
        assertEquals(functionName("net.codesup.util.emit.TestClass.AnotherClass.testFunction4"), TestClass.AnotherClass::testFunction4.qualifiedName)
        assertEquals(functionName("net.codesup.util.emit.TestClass.InnerClass.testFunction5"), TestClass.InnerClass::testFunction5.qualifiedName)
        assertEquals(functionName("net.codesup.util.emit.testFunction3"), String::testFunction3.qualifiedName)
        assertEquals(functionName("net.codesup.util.stream.testFunction6"), ::testFunction6.qualifiedName)
    }
}

fun testFunction1() {
    println("do nothing")
}

class TestClass {
    fun testFunction2(a: String) {
        println("do nothing")
    }

    class AnotherClass {
        fun testFunction4(a: String) {
            println("do nothing")
        }
    }
    inner class InnerClass {
        fun testFunction5(a: String) {
            println("do nothing")
        }
    }
}

fun String.testFunction3() {
    println("do nothing")
}


