package net.codesup.emit.expressions

import net.codesup.emit.SourceBuilder

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
open class BinaryExpression(override val sourceBuilder: SourceBuilder, token: String, lhs: Expression, rhs: Expression):
    NAryExpression(sourceBuilder, token, listOf(lhs, rhs))
