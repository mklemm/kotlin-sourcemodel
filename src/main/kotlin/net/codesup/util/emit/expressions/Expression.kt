package net.codesup.util.emit.expressions

import net.codesup.util.emit.Generable
import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.DeclarationScope
import net.codesup.util.emit.declaration.TypedElementDeclaration
import net.codesup.util.emit.use.SymbolUser

interface Expression : Generable, SymbolUser {
    val sourceBuilder: SourceBuilder
}


