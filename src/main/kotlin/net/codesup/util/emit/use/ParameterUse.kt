package net.codesup.util.emit.use

import net.codesup.util.emit.*
import net.codesup.util.emit.declaration.ParameterDeclaration

class ParameterUse(val declaration: ParameterDeclaration): Use<ParameterDeclaration>, ExpressionContext {
    override fun generate(output: OutputContext) {
        output.g(top)
    }

    val variable =  v(declaration.name)

    override var top: Expression? = variable

}
