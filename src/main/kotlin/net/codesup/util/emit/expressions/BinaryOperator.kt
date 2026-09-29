package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.DeclarationOwner

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
