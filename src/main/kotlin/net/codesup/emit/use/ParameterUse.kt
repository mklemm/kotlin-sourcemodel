package net.codesup.emit.use

import net.codesup.emit.Expression
import net.codesup.emit.ExpressionContext
import net.codesup.emit.OutputContext
import net.codesup.emit.declaration.ParameterDeclaration

class ParameterUse(val declaration: ParameterDeclaration): Use<ParameterDeclaration>, ExpressionContext {
    override fun generate(output: OutputContext) {
        output.g(top)
    }

    val variable =  v(declaration.name)

    override var top: Expression? = variable

}
