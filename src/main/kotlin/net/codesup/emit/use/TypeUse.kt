package net.codesup.emit.use

import net.codesup.emit.Annotatable
import net.codesup.emit.Expression
import net.codesup.emit.QualifiedName

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
abstract class TypeUse : Expression, Annotatable, SymbolUser {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        annotations.reportUsedSymbols(c)
    }

    override val annotations = mutableListOf<AnnotationUse>()
    var isNullable: Boolean = false
}

