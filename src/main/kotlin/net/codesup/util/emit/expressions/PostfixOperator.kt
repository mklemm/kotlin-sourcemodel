package net.codesup.util.emit.expressions

import net.codesup.util.emit.SourceBuilder

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
class PostfixOperator(sourceBuilder: SourceBuilder, operatorLiteral: String, operand: Expression) : UnaryExpression(sourceBuilder, operatorLiteral, operand)
