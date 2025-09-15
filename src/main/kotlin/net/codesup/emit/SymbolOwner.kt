package net.codesup.emit

/**
 * @author Mirko Klemm 2025-09-10
 *
 */
interface SymbolOwner {
    fun pathTo(symbol: Symbol): Sequence<Symbol>?
    fun qualifiedNameOf(declaration: Symbol): QualifiedName? =
        if(declaration is ExternalSymbol) declaration.qualifiedName
        else pathTo(declaration)?.map { it.name }?.let { QualifiedName(it.toList()) }
}
