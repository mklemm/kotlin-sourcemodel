package net.codesup.emit.use

import net.codesup.emit.Expression
import net.codesup.emit.ExpressionContext
import net.codesup.emit.OutputContext
import net.codesup.emit.declaration.PropertyDeclaration

class PropertyUse(val declaration: PropertyDeclaration) : Use<PropertyDeclaration>, ExpressionContext {
    val variable =  v(declaration.name)
    override var top: Expression? = variable
    override fun generate(output: OutputContext) {
        output.g(top)
    }
}
