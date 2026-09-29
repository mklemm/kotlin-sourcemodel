package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationOwner

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
