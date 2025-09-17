package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.declaration.ExternalTypeDeclaration

open class ExternalTypeUse(sourceBuilder: SourceBuilder, declaration: ExternalTypeDeclaration) : TypeUse(sourceBuilder, declaration), Use {

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        c.add(declaration)
        c.add(typeArguments)
    }

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        annotations.forEach { anno ->
            output.g(scope, anno).w(" ")
        }
        output.w(declaration.qualifiedName)
        output.list(scope, typeArguments, prefix = "<", suffix = ">")
        if (isNullable) output.w("?")
    }

}
