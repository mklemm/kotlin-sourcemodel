package net.codesup.util.emit.expressions

import net.codesup.util.emit.SourceBuilder

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
open class BinaryExpression(override val sourceBuilder: SourceBuilder, token: String, lhs: Expression, rhs: Expression):
    NAryExpression(sourceBuilder, token, listOf(lhs, rhs))
