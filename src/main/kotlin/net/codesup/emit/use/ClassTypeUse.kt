package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.DeclarationOwner

open class ClassTypeUse(sourceBuilder: SourceBuilder, declaration: ClassDeclaration) : TypeUse(sourceBuilder, declaration), Use {

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        c.add(declaration)
        c.add(typeArguments)
    }

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        annotations.forEach { anno ->
            output.g(scope, anno).w(" ")
        }
        output.w(sourceBuilder.qualifiedNameOf(declaration) ?: LocalName(declaration.name))
        if (isNullable) output.w("?")
        output.list(scope, typeArguments, prefix = "<", suffix = ">")
    }

    open fun copy(newDeclaration: ClassDeclaration = (declaration as ClassDeclaration), block: ClassTypeUse.() -> Unit = {}) =
        ClassTypeUse(sourceBuilder, newDeclaration).apply {
            this.isNullable = this@ClassTypeUse.isNullable
            this.annotations.addAll(this@ClassTypeUse.annotations)
            this.typeArguments.addAll(this@ClassTypeUse.typeArguments)
            block()
        }
}
