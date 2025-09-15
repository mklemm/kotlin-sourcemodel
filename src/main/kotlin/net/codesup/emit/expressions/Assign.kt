package net.codesup.emit.expressions

import net.codesup.emit.Generable
import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.CallableDeclaration
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.use.PropertyUse
import net.codesup.emit.use.SymbolUser

class Assign(sourceBuilder: SourceBuilder, val propertyUse: PropertyVar?, val expressionFactory: ExpressionFactory = ExpressionFactory(sourceBuilder)) :
    ExpressionContext by expressionFactory, Statement {

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        propertyUse?.generate(scope, output)
        output.w(" = ")
        expressionFactory.generate(scope, output)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        propertyUse?.reportUsedSymbols(c)
        expressionFactory.reportUsedSymbols(c)
    }
}
