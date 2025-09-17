package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationOwner

class Variable(context: SourceBuilder, val name: String) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.q(name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}

}
