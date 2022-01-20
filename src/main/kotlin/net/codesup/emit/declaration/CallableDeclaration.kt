package net.codesup.emit.declaration

import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.Assign
import net.codesup.emit.Block
import net.codesup.emit.Expression
import net.codesup.emit.QualifiedName

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
