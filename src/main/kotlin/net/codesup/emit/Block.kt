package net.codesup.emit

import net.codesup.emit.declaration.Declaration
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.FunctionDeclaration
import net.codesup.emit.declaration.PropertyDeclaration
import net.codesup.emit.declaration.TypedElementDeclaration
import net.codesup.emit.expressions.Assign
import net.codesup.emit.expressions.Expression
import net.codesup.emit.expressions.ExpressionContext
import net.codesup.emit.expressions.ExpressionFactory
import net.codesup.emit.expressions.PropertyVar
import net.codesup.emit.expressions.Statement
import net.codesup.emit.expressions.StatementContext
import net.codesup.emit.expressions.Variable

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
open class Block(val sourceBuilder: SourceBuilder) : StatementContext {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(statements)
    override val statements: MutableList<Statement> = mutableListOf()

    inline fun <reified T> ofType() = statements.filterIsInstance<T>()
    fun functions(): List<FunctionDeclaration> = statements.filterIsInstance<FunctionDeclaration>()

    fun _fun(name: String, block: FunctionDeclaration.() -> Unit) {
        addStatement(FunctionDeclaration(sourceBuilder, name).apply(block))
    }

    fun property(name: String, block: PropertyDeclaration.() -> Unit) {
        addStatement(PropertyDeclaration(sourceBuilder, name).apply(block))
    }

    fun _val(name: String, block: PropertyDeclaration.() -> Unit) {
        property(name) {
            isMutable = false
            block()
        }
    }
    fun _var(name: String, block: PropertyDeclaration.() -> Unit) {
        property(name) {
            isMutable = true
            block()
        }
    }

    operator fun (ExpressionContext.() -> Unit).unaryPlus() = addStatement(ExpressionFactory(sourceBuilder).apply(this))
    fun st(block:ExpressionContext.() -> Unit) = addStatement(ExpressionFactory(sourceBuilder).apply(block))

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.wl(" {")
        output.increaseIndent()
        statements.forEach {
            output.wi()
            it.generate(scope, output)
            output.wl()
        }
        output.decreaseIndent()
        output.wi().wl("}")
    }
}

