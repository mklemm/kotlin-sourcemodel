package net.codesup.emit.declaration

import net.codesup.emit.*
import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.SuperclassRef

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class ObjectDeclaration(name: String = "", sourceBuilder: SourceBuilder) : NamedDeclaration(sourceBuilder, name),
    DeclarationScope {
    override val annotations = mutableListOf<AnnotationUse>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        c.add(annotations, declarations, superTypes)
    }

    val modifiers = mutableSetOf<ObjectModifier>()
    val superTypes = mutableListOf<SuperclassRef>()
    override val declarations = mutableListOf<Declaration>()

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        annotations.forEach {
            it.generate(this, output)
            output.wl()
        }
        output.join(modifiers.map { it.value }, " ", "", " ")
            .w("object ")
            .q(name)
            .list(this, superTypes, prefix = ": ", suffix = " ")
        if (declarations.isNotEmpty()) {
            output.wl(" {")
            output.increaseIndent()
            declarations.forEach { output.g(this, it).wl() }
            output.decreaseIndent()
            output.wl()
            output.wl("}")
        }
    }

    fun _constructor(block: ConstructorDeclaration.() -> Unit) {
        declarations.add(ConstructorDeclaration(sourceBuilder).apply(block))
    }

    fun superType(qualifiedName: QualifiedName, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(
            SuperclassRef(
                sourceBuilder,
                sourceBuilder.typeUse(qualifiedName)
            )
                .apply(block)
        )
    }

    fun superType(typeUse: ClassTypeUse, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(sourceBuilder, typeUse).apply(block))
    }

    fun modifier(vararg mod: ObjectModifier) = modifiers.addAll(mod.toList())
    fun isPrivate() = modifier(ObjectModifier.PRIVATE)
    fun isPublic() = modifier(ObjectModifier.PUBLIC)
    fun isSealed() = modifier(ObjectModifier.SEALED)
    fun isCompanion() = modifier(ObjectModifier.COMPANION)
    override fun <D:Declaration> addDeclaration(declaration: D):D = declaration.also { declarations.add(it) }

}
