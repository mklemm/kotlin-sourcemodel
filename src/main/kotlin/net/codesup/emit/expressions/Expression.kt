package net.codesup.emit.expressions

import net.codesup.emit.Generable
import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.TypedElementDeclaration
import net.codesup.emit.use.SymbolUser

interface Expression : Generable, SymbolUser {
    val sourceBuilder: SourceBuilder
}


