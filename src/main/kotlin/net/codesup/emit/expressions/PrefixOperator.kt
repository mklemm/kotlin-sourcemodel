package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationOwner

class PrefixOperator(context: SourceBuilder, operatorLiteral: String, operand: Expression) : UnaryExpression(context, " $operatorLiteral", operand) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(token).g(scope, operands.first())
    }
}
