package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol

class GetterDeclaration(sourceBuilder: SourceBuilder) : CallableDeclaration(sourceBuilder) {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(block)
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(modifiers.joinToString(" "))
        output.w("get()")
        block?.generate(scope, output)
    }

}
