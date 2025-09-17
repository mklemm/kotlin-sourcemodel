package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.declaration.TypeDeclaration
import net.codesup.emit.expressions.ConstructorInv
import kotlin.reflect.KClass

class AnnotationUse(sourceBuilder: SourceBuilder, val type: TypeDeclaration, val isParameter: Boolean = false) : TypeUse(sourceBuilder, type) {
    constructor(sourceBuilder: SourceBuilder, a: Annotation, isParameter: Boolean = false):this(sourceBuilder,
        sourceBuilder.externalType(
            a.annotationClass
        )
    , isParameter)

    constructor(sourceBuilder: SourceBuilder, annotationClass: KClass<*>, isParameter: Boolean = false):this(sourceBuilder,
        sourceBuilder.externalType(annotationClass)
    , isParameter)

    var target: AnnotationUseSiteTarget? = null
    var constructorCall:ConstructorInv? = null

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        if(!isParameter) {
            output.w("@${if (target != null) "${target?.value ?: ""}:" else ""}")
        }
        constructorCall?.generate(scope, output) ?: output.w("()")
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(constructorCall)

}
