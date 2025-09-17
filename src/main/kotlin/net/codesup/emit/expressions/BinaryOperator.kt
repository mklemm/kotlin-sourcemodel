package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationOwner

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
class BinaryOperator(sourceBuilder: SourceBuilder, operatorLiteral: String, lhs: Expression, rhs: Expression) : BinaryExpression(sourceBuilder, " $operatorLiteral ", lhs, rhs) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w("(")
        super.generate(scope, output)
        output.w(")")
    }
}
