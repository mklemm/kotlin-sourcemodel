package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationOwner

class Lit(context: SourceBuilder, val a: Any) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(a.toString())
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}
}
