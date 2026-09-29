package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.DeclarationOwner

class PrefixOperator(context: SourceBuilder, operatorLiteral: String, operand: Expression) : UnaryExpression(context, " $operatorLiteral", operand) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(token).g(scope, operands.first())
    }
}
