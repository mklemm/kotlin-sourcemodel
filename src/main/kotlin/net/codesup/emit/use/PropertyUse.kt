package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.PropertyDeclaration
import net.codesup.emit.expressions.SingleExpr

class PropertyUse(sourceBuilder: SourceBuilder, val declaration: PropertyDeclaration) : SingleExpr(sourceBuilder), Use {
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w(declaration.name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        declaration.reportUsedSymbols(c)
    }
}
