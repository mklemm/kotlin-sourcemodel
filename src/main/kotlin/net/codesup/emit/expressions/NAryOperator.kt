package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationScope

class NAryOperator(context: SourceBuilder, val operatorLiteral: String, operands: List<Expression>) : NAryExpression(context, " $operatorLiteral ", operands) {
}
