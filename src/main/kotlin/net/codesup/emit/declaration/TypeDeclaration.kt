package net.codesup.emit.declaration

import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.SourceBuilder

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
abstract class TypeDeclaration(
    sourceBuilder: SourceBuilder,
    name: String,
): NamedDeclaration(sourceBuilder, name) {
    override val annotations = mutableListOf<AnnotationUse>()
    val modifiers = mutableSetOf<ClassModifier>()
    val typeParameters = mutableListOf<TypeParameterDeclaration>()
    override val doc: KDocBuilder = KDocBuilder()

    fun typeParam(name:String, block: TypeParameterDeclaration.() -> Unit) {
        typeParameters.add(TypeParameterDeclaration(sourceBuilder, name).apply(block))
    }
}
