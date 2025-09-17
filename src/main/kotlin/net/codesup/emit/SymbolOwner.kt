package net.codesup.emit

import net.codesup.emit.declaration.PackageDeclaration
import net.codesup.emit.declaration.TypeDeclaration

/**
 * @author Mirko Klemm 2025-09-10
 *
 */
interface SymbolOwner {
    fun pathTo(symbol: Symbol): Sequence<Symbol>?
    fun qualifiedNameOf(declaration: Symbol): QualifiedName? =
        if(declaration is ExternalSymbol)
            declaration.qualifiedName
        else pathTo(declaration)?.toList()?.let { path ->
                val packageParts = path.takeWhile { it is PackageDeclaration }
                val classParts = path.dropWhile { it is PackageDeclaration }.takeWhile { it is TypeDeclaration }
                if (classParts.last() == path.last()) {
                    ClassName(packageParts.flatMap { it.name.split('.') }, classParts.dropLast(1).map { it.name }, classParts.last().name)
                } else if (path.size > packageParts.size + classParts.size) {
                    FunctionName(packageParts.flatMap { it.name.split('.')}, classParts.map { it.name }, path.last().name)
                } else {
                    PackageName(packageParts.flatMap { it.name.split('.') }.dropLast(1).map { it }, packageParts.last().name)
                }
            }


}
