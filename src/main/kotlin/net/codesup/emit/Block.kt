package net.codesup.emit

import net.codesup.emit.declaration.FunctionDeclaration
import net.codesup.emit.declaration.PropertyDeclaration

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
open class Block : ContainerExpression, ExpressionContext {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(children)
    override val children: MutableList<Expression> = mutableListOf()
    override var top: Expression? = null
    override val context: ExpressionContext
        get() = this

    inline fun <reified T> ofType() = children.filterIsInstance<T>()
    fun functions(): List<FunctionDeclaration> = children.filterIsInstance<FunctionDeclaration>()

    fun function(name: String, block: FunctionDeclaration.() -> Unit) {
        top = FunctionDeclaration(name).apply(block)
    }

    fun property(name: String, block: PropertyDeclaration.() -> Unit) {
        top = PropertyDeclaration(name).apply(block)
    }

    override fun generate(output: OutputContext) {
        if (children.size == 1) {
            output.w(" = ")
        } else {
            output.wl(" {")
            output.increaseIndent()
        }
        output.g(top)
        //children.forEach { it.generate(output) }
        output.wl()
        if (children.size != 1) {
            output.decreaseIndent()
            output.wl("}")
        }
    }
}

