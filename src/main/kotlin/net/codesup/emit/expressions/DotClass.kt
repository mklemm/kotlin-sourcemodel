package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.use.TypeUse

class DotClass(context: SourceBuilder, val classTypeUse: TypeUse) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        classTypeUse.generate(scope, output)
        output.w("::class")
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = classTypeUse.reportUsedSymbols(c)

}
