package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.SymbolUser
import net.codesup.util.emit.*

/**
 * @author Mirko Klemm 2021-12-22
 *
 */
class SuperclassRef(val classTypeUse: ClassTypeUse): SymbolUser, Generable, ExpressionContext {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        c.add(classTypeUse)
        c.add(top)
    }
    override var top: Expression? = null

    override fun generate(output: OutputContext) {
        (top ?: classTypeUse).generate(output)
    }

    fun cons(block: ConstructorInv.() -> Unit = {}) = ConstructorInv(this, classTypeUse).apply(block).also { top = it }
}
