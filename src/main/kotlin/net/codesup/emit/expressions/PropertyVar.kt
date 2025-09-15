package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.TypedElementDeclaration

class PropertyVar(context: SourceBuilder, val propertyDeclaration: TypedElementDeclaration) : SingleExpr(context) {
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        if(scope.declarations.contains(propertyDeclaration)) {
            output.w("this.")
        }
        output.q(propertyDeclaration.name)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {}

}
