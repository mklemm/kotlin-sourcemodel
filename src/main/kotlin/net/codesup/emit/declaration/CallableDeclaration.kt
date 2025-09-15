package net.codesup.emit.declaration

import net.codesup.emit.Block
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.expressions.Statement
import net.codesup.emit.use.AnnotationUse

/**
 * @author Mirko Klemm 2021-03-18
 *
 */

abstract class CallableDeclaration(override val sourceBuilder: SourceBuilder) : Declaration {
    override var metadata: Any? = null
    override val name: String get() = ""
    override val annotations = mutableListOf<AnnotationUse>()
    val modifiers = mutableListOf<String>()
    var block: Block? = null

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        c.add(annotations)
        c.add(block)
    }

    fun block(block: Block.() -> Unit) {
        this.block = Block(sourceBuilder).apply(block)
    }

}
