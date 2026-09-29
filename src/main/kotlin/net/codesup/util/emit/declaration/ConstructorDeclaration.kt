package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.Parameterized
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol

class ConstructorDeclaration(sourceBuilder: SourceBuilder) : CallableDeclaration(sourceBuilder), Parameterized {
    override val parameters = mutableListOf<ParameterDeclaration>()

    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
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
