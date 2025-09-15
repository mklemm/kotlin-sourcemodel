package net.codesup.emit.declaration

import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.use.SuperclassRef

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class ClassDeclaration(
    sourceBuilder: SourceBuilder,
    name: String,
    val classifierKind: ClassifierKind = ClassifierKind.CLASS
)
    : TypeDeclaration(sourceBuilder, name), DeclarationScope {
    override val annotations = mutableListOf<AnnotationUse>()
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        c.add(annotations, declarations, typeParameters, superTypes)
        c.add(primaryConstructor)
    }
    val superTypes = mutableListOf<SuperclassRef>()
    var primaryConstructor: PrimaryConstructorDeclaration? = null
    override val declarations = mutableListOf<Declaration>()
    override val doc: KDocBuilder = KDocBuilder()

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        doc.generate(this, output)
        annotations.forEach {
            it.generate(this, output)
            output.wl()
        }
        output.join(modifiers.map { it.value }," ", "", " ")
            .w("${classifierKind.keyword} ").q(name).list(this, typeParameters, prefix = "<", suffix = ">")
            .g(this, primaryConstructor)
            .list(this, superTypes, prefix = ": ", suffix = " ")
            .list(this, typeParameters.flatMap { it.fullBoundaries }, prefix = " where ", suffix = " ")
        if(primaryConstructor?.block != null || declarations.isNotEmpty()) {
            output.wl(" {")
            output.increaseIndent()
            output.g(this, primaryConstructor?.block).wl()
            declarations.forEach {
                output.wi().g(this, it).wl()
            }
            output.decreaseIndent()
            output.wl("}")
        }
    }

    fun primaryConstructor(block: PrimaryConstructorDeclaration.() -> Unit) {
        this.primaryConstructor = PrimaryConstructorDeclaration(sourceBuilder).apply(block)
    }

    fun _constructor(block: ConstructorDeclaration.() -> Unit = {}): ConstructorDeclaration =
        ConstructorDeclaration(sourceBuilder).apply(block).also { declarations.add(it) }


    fun _object(name: String = "", block: ObjectDeclaration.() -> Unit = {}): ObjectDeclaration = declarations.singleOrNull { it is ObjectDeclaration && it.name == name }?.let { it as ObjectDeclaration } ?: ObjectDeclaration(
        name,
        sourceBuilder
    ).apply(block).also{declarations.add(it)}

    fun _companion(name: String = "", block: ObjectDeclaration.() -> Unit = {}): ObjectDeclaration = declarations.singleOrNull { it is ObjectDeclaration && it.name == name }?.let { it as ObjectDeclaration } ?: ObjectDeclaration(
        name,
        sourceBuilder
    ).apply { isCompanion() }.apply(block).also{declarations.add(it)}

    fun superType(qualifiedName: QualifiedName, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(
            sourceBuilder,
            sourceBuilder.typeUse(qualifiedName)).apply(block))

    }


    fun superType(typeUse: ClassTypeUse, block: SuperclassRef.() -> Unit = {}) {
        superTypes.add(SuperclassRef(sourceBuilder, typeUse).apply(block))
    }

    fun modifier(vararg mod: ClassModifier) = modifiers.addAll(mod.toList())
    fun isOpen() = modifier(ClassModifier.OPEN)
    fun isPrivate() = modifier(ClassModifier.PRIVATE)
    fun isPublic() = modifier(ClassModifier.PUBLIC)
    fun isSealed() = modifier(ClassModifier.SEALED)

    override fun addDeclaration(declaration: Declaration) {
        declarations.add(declaration)
    }
}
