package net.codesup.util.emit.declaration

import net.codesup.util.emit.Annotatable
import net.codesup.util.emit.Generable
import net.codesup.util.emit.LocalName
import net.codesup.util.emit.expressions.Expression
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.expressions.Statement
import net.codesup.util.emit.use.SymbolUser
import net.codesup.util.emit.use.Use

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
    val qualifiedName: QualifiedName get() = sourceBuilder.qualifiedNameOf(this) ?: LocalName(name)
    fun doc(s: String): KDocBuilder = doc.apply { lines.add(s) }
    fun doc(block: KDocBuilder.() -> Unit) = doc.apply(block)
}
