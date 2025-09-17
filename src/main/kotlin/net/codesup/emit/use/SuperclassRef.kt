package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.expressions.ConstructorInv
import net.codesup.emit.expressions.SingleExpr

/**
 * @author Mirko Klemm 2021-12-22
 *
 */
class SuperclassRef(override val sourceBuilder: SourceBuilder, val classTypeUse: TypeUse): SingleExpr(sourceBuilder) {
    var constructorInvocation: ConstructorInv? = null

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        c.add(classTypeUse)
    }

    fun cons(block: ConstructorInv.() -> Unit = {}) = ConstructorInv(sourceBuilder, classTypeUse.declaration).apply(block).also { constructorInvocation = it }

    override fun generate(
        scope: DeclarationOwner,
        output: OutputContext
    ) {
        classTypeUse.generate(scope, output)
        constructorInvocation?.generate(scope, output)
    }
}
