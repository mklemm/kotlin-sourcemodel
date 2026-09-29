package net.codesup.util.emit.use

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationOwner
import net.codesup.util.emit.declaration.PropertyDeclaration
import net.codesup.util.emit.expressions.SingleExpr

class PropertyUse(sourceBuilder: SourceBuilder, val declaration: PropertyDeclaration) : SingleExpr(sourceBuilder), Use {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(declaration.name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        declaration.reportUsedSymbols(c)
    }
}
