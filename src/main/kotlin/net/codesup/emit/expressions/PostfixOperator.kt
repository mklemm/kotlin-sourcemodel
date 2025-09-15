package net.codesup.emit.expressions

import net.codesup.emit.SourceBuilder

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
class PostfixOperator(sourceBuilder: SourceBuilder, operatorLiteral: String, operand: Expression) : UnaryExpression(sourceBuilder, operatorLiteral, operand)
