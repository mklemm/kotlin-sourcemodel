package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.Declaration
import net.codesup.emit.declaration.DeclarationOwner

class PropertyVar(context: SourceBuilder, val propertyDeclaration: Declaration) : SingleExpr(context) {
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        if(scope.declarations.contains(propertyDeclaration)) {
            output.w("this.")
        }
        output.q(propertyDeclaration.name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}

}
