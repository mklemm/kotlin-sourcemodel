package net.codesup.util.emit.declaration

import net.codesup.util.emit.use.AnnotationUse
import net.codesup.util.emit.Assign
import net.codesup.util.emit.Block
import net.codesup.util.emit.Expression
import net.codesup.util.emit.QualifiedName

/**
 * @author Mirko Klemm 2021-03-18
 *
 */

abstract class CallableDeclaration<T: CallableDeclaration<T>>: Declaration<T> {
    override val name: String get() = ""
    override val declarations: MutableList<Declaration<*>> = mutableListOf()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        c.add(annotations)
        c.add(block)
    }
    override val annotations = mutableListOf<AnnotationUse>()
    val modifiers = mutableListOf<String>()
    var block: Expression? = null

    fun block(block: Block.()->Unit) {
        this.block = Block().apply(block)
    }
    fun assign(blk: Assign.() -> Unit) {
        this.block = Assign().apply(blk)
    }
}
