package net.codesup.util.emit.declaration

import net.codesup.util.emit.Block
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.expressions.Statement
import net.codesup.util.emit.use.AnnotationUse

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
