package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationScope

class Str(context: SourceBuilder, val content: String) : SingleExpr(context) {
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w("\"").w(content).w("\"")
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}
}
