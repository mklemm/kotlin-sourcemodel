package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.Parameterized
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol

class ConstructorDeclaration(sourceBuilder: SourceBuilder) : CallableDeclaration(sourceBuilder), Parameterized {
    override val parameters = mutableListOf<ParameterDeclaration>()

    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        doc.generate(scope, output)
        modifiers.forEach {
            output.w(it).w(" ")
        }
        output.w("constructor ")
        output.list(scope, parameters, prefix = "(", suffix = ")")
        output.g(scope, block)
    }

    internal fun generateBlock(output: OutputContext) {

    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        parameters.reportUsedSymbols(c)
    }

}
