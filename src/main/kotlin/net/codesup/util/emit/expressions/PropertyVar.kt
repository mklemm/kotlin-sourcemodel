package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.Declaration
import net.codesup.util.emit.declaration.DeclarationOwner

class PropertyVar(context: SourceBuilder, val propertyDeclaration: Declaration) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        if(scope.declarations.contains(propertyDeclaration)) {
            output.w("this.")
        }
        output.q(propertyDeclaration.name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}

}
