package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationOwner

class ListExpr(
    context: SourceBuilder,
    sep: String,
    val prefix: String,
    val suffix: String,
    operands: List<Expression>
) :
    NAryExpression(context, sep, operands) {

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.list(scope, operands, token, prefix, suffix)
    }
}
