package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationScope

class ListExpr(
    context: SourceBuilder,
    sep: String,
    val prefix: String,
    val suffix: String,
    operands: List<Expression>
) :
    NAryExpression(context, sep, operands) {

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.list(scope, operands, token, prefix, suffix)
    }
}
