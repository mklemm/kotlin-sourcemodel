package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.DeclarationScope

class NAryOperator(context: SourceBuilder, val operatorLiteral: String, operands: List<Expression>) : NAryExpression(context, " $operatorLiteral ", operands) {
}
