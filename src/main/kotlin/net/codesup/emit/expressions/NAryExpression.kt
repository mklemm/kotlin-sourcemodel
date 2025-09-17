package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationOwner

abstract class NAryExpression(override val sourceBuilder: SourceBuilder, val token: String, val operands: List<Expression>) : Expression {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        var first = true;
        for(operand in operands) {
            if(first) {
                first = false
            } else {
                output.w(token)
            }
            output.g(scope, operand)
        }
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        operands.reportUsedSymbols(c)
    }
}
