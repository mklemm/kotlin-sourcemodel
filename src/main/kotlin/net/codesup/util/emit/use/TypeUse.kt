package net.codesup.util.emit.use

import net.codesup.util.emit.Annotatable
import net.codesup.util.emit.Expression
import net.codesup.util.emit.QualifiedName

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

