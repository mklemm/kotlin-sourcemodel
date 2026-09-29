package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationOwner
import net.codesup.util.emit.use.TypeUse

class DotClass(context: SourceBuilder, val classTypeUse: TypeUse) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        classTypeUse.generate(scope, output)
        output.w("::class")
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = classTypeUse.reportUsedSymbols(c)

}
