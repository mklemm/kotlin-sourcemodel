package net.codesup.emit.declaration

import net.codesup.emit.SourceFile

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface DeclarationScope : DeclarationOwner {

    fun addDeclaration(declaration: Declaration)

    fun _property(name: String, block: PropertyDeclaration.() -> Unit): PropertyDeclaration =
        PropertyDeclaration(sourceBuilder, name).apply(block).also { addDeclaration(it) }

    fun _val(name: String, block: PropertyDeclaration.() -> Unit): PropertyDeclaration =
        PropertyDeclaration(sourceBuilder, name).apply { isMutable = false }.apply(block).also { addDeclaration(it) }

    fun _var(name: String, block: PropertyDeclaration.() -> Unit): PropertyDeclaration =
        PropertyDeclaration(sourceBuilder, name).apply { isMutable = true }.apply(block).also { addDeclaration(it) }

    fun _fun(name: String, block: FunctionDeclaration.() -> Unit): FunctionDeclaration =
        FunctionDeclaration(sourceBuilder, name).apply(block).also { addDeclaration(it) }

    fun _class(name: String, block: ClassDeclaration.() -> Unit = {}): ClassDeclaration =
        ClassDeclaration(sourceBuilder, name).apply(block).also { addDeclaration(it) }

    fun _classifier(
        name: String,
        classifierKind: ClassifierKind,
        block: ClassDeclaration.() -> Unit = {}
    ): ClassDeclaration = ClassDeclaration(sourceBuilder, name, classifierKind).apply(block).also { addDeclaration(it) }

    fun _interface(name: String, block: ClassDeclaration.() -> Unit): ClassDeclaration =
        ClassDeclaration(sourceBuilder, name, ClassifierKind.INTERFACE).apply(block).also { addDeclaration(it) }

    fun sourceFile(packageName: PackageDeclaration, name: String, block: SourceFile.() -> Unit) {
        addDeclaration(SourceFile(sourceBuilder, packageName, name).apply(block))
    }


}
