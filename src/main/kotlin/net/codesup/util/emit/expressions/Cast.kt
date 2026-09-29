package net.codesup.util.emit.expressions

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.DeclarationScope
import net.codesup.util.emit.use.TypeUse

class Cast(context: SourceBuilder, operand: Expression, typeUse: TypeUse) : BinaryExpression(context, " as ", operand, typeUse)
class NullableCast(context: SourceBuilder, operand: Expression, typeUse: TypeUse) : BinaryExpression(context, " as? ", operand, typeUse)
