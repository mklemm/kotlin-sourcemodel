package net.codesup.util.emit.use

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.*

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
