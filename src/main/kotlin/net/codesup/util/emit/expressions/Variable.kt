package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationOwner

class Variable(context: SourceBuilder, val name: String) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.q(name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}

}
