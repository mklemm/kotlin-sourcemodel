# kotlin-sourcemodel
Source Code Builder for the Kotlin programming language.
This is still work in progress.

## Motivation
There are already a couple of source code builders/source generators for kotlin out there, but so far as I know, none of them really leverages the powers of kotlin, like typesafe builder DSL, operator overloading etc.

**kotlin-sourcemodel** strives to be a simple, yet powerful and intuitive library for kotlin sourcecode generation, using kotlin to generate kotlin.

It also tries to automate things that are creating a mess when doing
simple "println()"-style code generation, like generating import statements
on the kotlin source file level and automatically stripping package prefixes
from imported symbols.

For the generation of expressions, it uses operator overloading to make
the task as intuitive as possible.

Each generated code element is an object that can be reused throughout the
generation domain, for example, you can generate a class declaration and then
use that declaration somewhere else as a type reference.

## Features
 - Builder-DSL for all source constructs
 - generated elements are reusable and can be passed around as objects
 - Automatic quoting of reserved words
 - Automatic derivation of import statements from used symbols
 - Symbol name conflict resolution
 - Operator overloading for intuitive expression generation

## Limitations
kotlin-sourcemodel cannot completely guarantee syntactical correctness of the generated code,
this ist partly still up to the user. With future versions, things will be improved in that respect.
Currently, there also isn't a reverse parser,
there are other projects providing this.

There are still code constructs missing support, but it will be implemented as needed.

## Examples
The following code sample builds a package with a file declaring a class with a property (declared as primary constructor parameter)
and a function using external types:
````kotlin
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
````

The above section generates the following code (which doesn't
make too much sense, but hey, it's an example:
```kotlin
package net.codesup.util

import kotlin.String
import kotlin.Unit

import net.codesup.util.AnotherClass

class TestClass<T: Any?.() -> AnotherClass<T>>(val firstParam: AnotherClass) {

    var `var`: String
    val property2: AnotherClass<T> = ((x - 1) * y)

}
```
