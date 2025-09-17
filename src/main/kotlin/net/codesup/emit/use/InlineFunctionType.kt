package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.*

class InlineFunctionType(sourceBuilder: SourceBuilder, ftd: FunctionTypeDeclaration = FunctionTypeDeclaration(sourceBuilder)) :
    TypeUse(sourceBuilder, ftd), FunctionTypeSupport by ftd {

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        declaration.reportUsedSymbols(c)
    }

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        if (isNullable) output.w("(")
        declaration.generate(scope, output)
        if (isNullable) output.w(")?")
    }

    fun nullable(nl: Boolean = true) {
        isNullable = nl
    }


}
