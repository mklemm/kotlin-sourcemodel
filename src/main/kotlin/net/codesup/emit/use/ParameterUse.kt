package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.declaration.ParameterDeclaration
import net.codesup.emit.expressions.SingleExpr

class ParameterUse(sourceBuilder: SourceBuilder, val declaration: ParameterDeclaration):SingleExpr(sourceBuilder), Use {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(declaration.name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        declaration.reportUsedSymbols(c)
    }

}
