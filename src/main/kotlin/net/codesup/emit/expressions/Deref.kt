package net.codesup.emit.expressions

import net.codesup.emit.SourceBuilder

class Deref(context: SourceBuilder, lhs: Expression, rhs: Expression) : BinaryExpression(context, ".", lhs, rhs)
