package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.use.TypeUse

class Cast(context: SourceBuilder, operand: Expression, typeUse: TypeUse) : BinaryExpression(context, " as ", operand, typeUse)
class NullableCast(context: SourceBuilder, operand: Expression, typeUse: TypeUse) : BinaryExpression(context, " as? ", operand, typeUse)
