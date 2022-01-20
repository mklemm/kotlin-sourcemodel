package net.codesup.emit.declaration

import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.use.SuperclassRef
import net.codesup.emit.use.Use

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class ObjectDeclaration(override val name: String = "") : NamedDeclaration<ObjectDeclaration>,
    DeclarationOwner<ObjectDeclaration> {
    override val annotations = mutableListOf<AnnotationUse>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        c.add(annotations, declarations, superTypes)
    }
    val modifiers = mutableSetOf<ObjectModifier>()
    val superTypes = mutableListOf<SuperclassRef>()
    override val declarations = mutableListOf<Declaration<*>>()

    override fun use(block: Use<ObjectDeclaration>.() -> Unit): Use<ObjectDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<ObjectDeclaration>.() -> Unit): Use<ObjectDeclaration> {
        TODO("Not yet implemented")
    }

    override fun generate(output: OutputContext) {
        annotations.forEach {
            it.generate(output)
            output.wl()
        }
        output.join(modifiers.map { it.value }," ", "", " ")
            .w("object ")
            .q(name)
            .list(superTypes, ", ", ": ", " ")
        if(declarations.isNotEmpty()) {
            output.wl(" {")
            output.increaseIndent()
            declarations.forEach { output.g(it).wl() }
            output.decreaseIndent()
            output.wl()
            output.wl("}")
        }
    }

    fun _constructor(block: ConstructorDeclaration.() -> Unit) {
        declarations.add(ConstructorDeclaration().apply(block))
    }

    fun superType(qualifiedName: QualifiedName, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(ClassTypeUse(qualifiedName)).apply(block))
    }

    fun superType(typeUse: ClassTypeUse, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(typeUse).apply(block))
    }

    fun modifier(vararg mod: ObjectModifier) = modifiers.addAll(mod.toList())
    fun isPrivate() = modifier(ObjectModifier.PRIVATE)
    fun isPublic() = modifier(ObjectModifier.PUBLIC)
    fun isSealed() = modifier(ObjectModifier.SEALED)
    fun isCompanion() = modifier(ObjectModifier.COMPANION)
}
