package net.codesup.emit.expressions

import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.TypeDeclaration

class ConstructorInv(context: SourceBuilder, val classDeclaration: TypeDeclaration) : Invocation(context, classDeclaration)
