package net.codesup.emit.use

import net.codesup.emit.Annotatable
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.TypeDeclaration
import net.codesup.emit.expressions.SingleExpr

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
abstract class TypeUse(sourceBuilder: SourceBuilder, val declaration: TypeDeclaration) : SingleExpr(sourceBuilder), Annotatable, SymbolUser, Use {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        annotations.reportUsedSymbols(c)
    }
    override val annotations = mutableListOf<AnnotationUse>()
    var isNullable: Boolean = false
}

