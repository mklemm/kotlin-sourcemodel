package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationOwner

class Assign(sourceBuilder: SourceBuilder, val propertyUse: PropertyVar?, val expressionFactory: ExpressionFactory = ExpressionFactory(sourceBuilder)) :
    ExpressionContext by expressionFactory, Statement {

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        propertyUse?.generate(scope, output)
        output.w(" = ")
        expressionFactory.generate(scope, output)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        propertyUse?.reportUsedSymbols(c)
        expressionFactory.reportUsedSymbols(c)
    }
}
