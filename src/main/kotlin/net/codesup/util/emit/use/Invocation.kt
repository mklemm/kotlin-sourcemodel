package net.codesup.util.emit.use

import net.codesup.util.emit.Expression
import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.declaration.FunctionDeclaration

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
open class Invocation(val owner: Expression): Expression, Use<FunctionDeclaration> {
    val arguments:MutableList<Expression> = mutableListOf()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = arguments.reportUsedSymbols(c)

    override fun generate(output: OutputContext) {
        output.w("(")
        output.list(arguments)
        output.w(")")
    }
}
