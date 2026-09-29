package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.DeclarationOwner

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
