package net.codesup.util.emit.use

import net.codesup.util.emit.QualifiedName

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
interface SymbolUser {
    fun reportUsedSymbols(c:MutableCollection<QualifiedName>)
    fun Iterable<SymbolUser>.reportUsedSymbols(c:MutableCollection<QualifiedName>) = forEach { it.reportUsedSymbols(c) }
    fun MutableCollection<QualifiedName>.add(vararg symbolUser:SymbolUser?) = symbolUser.toList().forEach { it?.reportUsedSymbols(this) }
    fun MutableCollection<QualifiedName>.add(vararg users: Iterable<SymbolUser>?) = users.forEach { it?.reportUsedSymbols(this) }
}


