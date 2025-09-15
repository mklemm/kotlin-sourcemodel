package net.codesup.emit.declaration

import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.SymbolOwner

/**
 * @author Mirko Klemm 2025-09-10
 *
 */
interface DeclarationOwner: SymbolOwner {
    val sourceBuilder: SourceBuilder
    val declarations: MutableList<out Declaration>
    override fun pathTo(symbol: Symbol): Sequence<Symbol>? = declarations.firstNotNullOfOrNull { it.pathTo(symbol) }
}
