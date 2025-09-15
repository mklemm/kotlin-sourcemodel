package net.codesup.emit.use

import net.codesup.emit.Symbol

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
interface SymbolUser {
    fun reportUsedSymbols(c: MutableCollection<Symbol>)
    fun Iterable<SymbolUser>.reportUsedSymbols(c: MutableCollection<Symbol>) = forEach { it.reportUsedSymbols(c) }
    fun MutableCollection<Symbol>.add(vararg symbolUser: SymbolUser?) = symbolUser.toList().forEach { it?.reportUsedSymbols(this) }
    fun MutableCollection<Symbol>.add(vararg users: Iterable<SymbolUser>?) = users.forEach { it?.reportUsedSymbols(
        this
    ) }
}


