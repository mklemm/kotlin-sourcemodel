package net.codesup.emit.use

import net.codesup.emit.Annotatable
import net.codesup.emit.expressions.Expression
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.*
import kotlin.reflect.KClass

class InlineFunctionType(sourceBuilder: SourceBuilder, ftd: FunctionTypeDeclaration = FunctionTypeDeclaration(sourceBuilder)) :
    TypeUse(sourceBuilder, ftd), FunctionTypeSupport by ftd {

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        declaration.reportUsedSymbols(c)
    }

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        if (isNullable) output.w("(")
        declaration.generate(scope, output)
        if (isNullable) output.w(")?")
    }

    fun nullable(nl: Boolean = true) {
        isNullable = nl
    }


}
