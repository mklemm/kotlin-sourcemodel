package net.codesup.emit.expressions

import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
abstract class UnaryExpression(override val sourceBuilder: SourceBuilder, token: String, val operand: Expression):NAryExpression(sourceBuilder, token, listOf(operand))
