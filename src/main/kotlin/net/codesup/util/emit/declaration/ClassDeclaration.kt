package net.codesup.util.emit.declaration

import net.codesup.util.emit.use.AnnotationUse
import net.codesup.util.emit.use.ClassTypeUse
import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.use.SuperclassRef
import net.codesup.util.emit.use.Use

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class ClassDeclaration(override val name: String, val keyword:String = "class") : NamedDeclaration<ClassDeclaration>, DeclarationOwner<ClassDeclaration> {
    override val annotations = mutableListOf<AnnotationUse>()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        c.add(annotations, declarations, typeParameters, superTypes)
        c.add(primaryConstructor)
    }
    val modifiers = mutableSetOf<ClassModifier>()
    val superTypes = mutableListOf<SuperclassRef>()
    var primaryConstructor: PrimaryConstructorDeclaration? = null
    val typeParameters = mutableListOf<TypeParamDeclaration>()
    override val declarations = mutableListOf<Declaration<*>>()
    override val doc: KDocBuilder = KDocBuilder()

    override fun use(block: Use<ClassDeclaration>.() -> Unit): ClassTypeUse {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<ClassDeclaration>.() -> Unit): ClassTypeUse {
        TODO("Not yet implemented")
    }

    override fun generate(output: OutputContext) {
        doc.generate(output)
        annotations.forEach {
            it.generate(output)
            output.wl()
        }
        output.join(modifiers.map { it.value }," ", "", " ")
            .w("$keyword ").q(name).list(typeParameters, ", ", "<", ">")
            .g(primaryConstructor)
            .list(superTypes, ", ", ": ", " ")
            .list(typeParameters.flatMap { it.fullBoundaries }, ", ", " where ", " ")
        if(primaryConstructor?.block != null || declarations.isNotEmpty()) {
            output.wl(" {")
            output.increaseIndent()
            output.g(primaryConstructor?.block).wl()
            declarations.forEach { output.g(it).wl() }
            output.decreaseIndent()
            output.wl()
            output.wl("}")
        }
    }

    fun primaryConstructor(block: PrimaryConstructorDeclaration.() -> Unit) {
        this.primaryConstructor = PrimaryConstructorDeclaration().apply(block)
    }

    fun _constructor(block: ConstructorDeclaration.() -> Unit = {}):ConstructorDeclaration =
        ConstructorDeclaration().apply(block).also { declarations.add(it) }


    fun _object(name: String = "", block:ObjectDeclaration.() -> Unit = {}):ObjectDeclaration = declarations.singleOrNull { it is ObjectDeclaration && it.name == name }?.let { it as ObjectDeclaration} ?: ObjectDeclaration(name).apply(block).also{declarations.add(it)}

    fun _companion(name: String = "", block:ObjectDeclaration.() -> Unit = {}):ObjectDeclaration = declarations.singleOrNull { it is ObjectDeclaration && it.name == name }?.let { it as ObjectDeclaration} ?: ObjectDeclaration(name).apply { isCompanion() }.apply(block).also{declarations.add(it)}

    fun typeParam(name:String, block: TypeParamDeclaration.() -> Unit) {
        typeParameters.add(TypeParamDeclaration(name).apply(block))
    }

    fun superType(qualifiedName: QualifiedName, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(ClassTypeUse(qualifiedName)).apply(block))
    }

    fun superType(typeUse: ClassTypeUse, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(typeUse).apply(block))
    }

    fun modifier(vararg mod:ClassModifier) = modifiers.addAll(mod.toList())
    fun isOpen() = modifier(ClassModifier.OPEN)
    fun isPrivate() = modifier(ClassModifier.PRIVATE)
    fun isPublic() = modifier(ClassModifier.PUBLIC)
    fun isSealed() = modifier(ClassModifier.SEALED)

}
