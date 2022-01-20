package net.codesup.util.emit.use

import net.codesup.util.emit.Expression
import net.codesup.util.emit.ExpressionContext
import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.declaration.PropertyDeclaration

class PropertyUse(val declaration: PropertyDeclaration) : Use<PropertyDeclaration>, ExpressionContext {
    val variable =  v(declaration.name)
    override var top: Expression? = variable
    override fun generate(output: OutputContext) {
        output.g(top)
    }
}
