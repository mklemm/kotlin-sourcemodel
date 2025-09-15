package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol

class SetterDeclaration(sourceBuilder: SourceBuilder, val parameterName:String = "value"): CallableDeclaration(sourceBuilder) {
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w(modifiers.joinToString(" "))
        output.w("set(").q(parameterName).w(")")
        block?.generate(scope, output)
    }

    override fun pathTo(symbol: Symbol): Sequence<Symbol>? {
        return null
    }
}
