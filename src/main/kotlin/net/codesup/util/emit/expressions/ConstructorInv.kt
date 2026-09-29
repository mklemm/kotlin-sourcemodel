package net.codesup.util.emit.expressions

import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.TypeDeclaration

class ConstructorInv(context: SourceBuilder, val classDeclaration: TypeDeclaration) : Invocation(context, classDeclaration)
