package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.ExternalTypeDeclaration
import net.codesup.emit.declaration.FunctionTypeDeclaration
import net.codesup.emit.declaration.KClassDeclaration
import net.codesup.emit.declaration.TypeDeclaration
import net.codesup.emit.declaration.TypeParameterDeclaration
import net.codesup.emit.expressions.DotClass
import net.codesup.emit.expressions.ExpressionContext
import kotlin.reflect.KClass

open class ExternalTypeUse(sourceBuilder: SourceBuilder, declaration: ExternalTypeDeclaration) : TypeUse(sourceBuilder, declaration), Use {

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        c.add(declaration)
        c.add(typeArguments)
    }

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        annotations.forEach { anno ->
            output.g(scope, anno).w(" ")
        }
        output.w(declaration.qualifiedName)
        output.list(scope, typeArguments, prefix = "<", suffix = ">")
        if (isNullable) output.w("?")
    }

}
