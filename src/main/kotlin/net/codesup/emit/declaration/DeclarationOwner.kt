package net.codesup.emit.declaration

import net.codesup.emit.SourceFile

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface DeclarationOwner<T: Declaration<T>>: Declaration<T> {

    fun _property(name:String, block: PropertyDeclaration.() -> Unit): PropertyDeclaration = PropertyDeclaration(name).apply(block).also { declarations.add(it) }

    fun _val(name:String, block: PropertyDeclaration.() -> Unit): PropertyDeclaration = PropertyDeclaration(name).apply { isMutable = false }.apply(block).also { declarations.add(it) }

    fun _var(name:String, block: PropertyDeclaration.() -> Unit): PropertyDeclaration = PropertyDeclaration(name).apply { isMutable = true }.apply(block).also { declarations.add(it) }

    fun _fun(name:String, block: FunctionDeclaration.() -> Unit): FunctionDeclaration = FunctionDeclaration(name).apply(block).also { declarations.add(it) }

    fun _class(name:String, block: ClassDeclaration.() -> Unit): ClassDeclaration = ClassDeclaration(name).apply(block).also { declarations.add(it) }

    fun _interface(name:String, block: ClassDeclaration.() -> Unit): ClassDeclaration = ClassDeclaration(name, "interface").apply(block).also { declarations.add(it) }

    fun sourceFile(packageName: Package, name:String, block: SourceFile.() -> Unit) {
        declarations.add(SourceFile(packageName, name).apply(block))
    }

}
