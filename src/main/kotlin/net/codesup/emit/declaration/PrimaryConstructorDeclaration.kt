package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol

class PrimaryConstructorDeclaration(sourceBuilder: SourceBuilder) : CallableDeclaration(sourceBuilder) {
    val parameters = mutableListOf<TypedElementDeclaration>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        if(modifiers.isNotEmpty()) {
            modifiers.forEach {
                output.w(it).w(" ")
            }
            output.w("constructor ")
        }
        output.list(scope, parameters, prefix = "(", suffix = ")")
    }

    fun generateBlock(scope: DeclarationScope, output: OutputContext) {
        block?.let { b ->
            output.wl("init ")
            output.g(scope, b)
        }
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
            super.reportUsedSymbols(c)
            parameters.reportUsedSymbols(c)
        }

    fun param(name:String, block: ParameterDeclaration.() -> Unit) = ParameterDeclaration(
        sourceBuilder,
        name
    ).apply(block).also{parameters.add(it)}

    fun property(name:String, block: PropertyDeclaration.() -> Unit) = PropertyDeclaration(
        sourceBuilder,
        name
    ).apply(block).also{parameters.add(it)}

    override fun pathTo(symbol: Symbol): Sequence<Symbol>? {
        return null
    }
}
