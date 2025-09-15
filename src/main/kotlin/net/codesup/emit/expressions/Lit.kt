package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationScope

class Lit(context: SourceBuilder, val a: Any) : SingleExpr(context) {
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w(a.toString())
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}
}
