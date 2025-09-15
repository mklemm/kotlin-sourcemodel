package net.codesup.emit.declaration

import net.codesup.emit.Annotatable
import net.codesup.emit.Generable
import net.codesup.emit.expressions.Expression
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.expressions.Statement
import net.codesup.emit.use.SymbolUser
import net.codesup.emit.use.Use

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface Declaration : Annotatable, Symbol, Statement {
    val doc: KDocBuilder
    var metadata: Any?

    fun reportDeclaredSymbols(c: MutableCollection<Symbol>) {
        c.add(this)
    }

    fun doc(s: String): KDocBuilder = doc.apply { lines.add(s) }
    fun doc(block: KDocBuilder.() -> Unit) = doc.apply(block)
}
