package net.codesup.util.emit.expressions

import net.codesup.util.emit.SourceBuilder

class Deref(context: SourceBuilder, lhs: Expression, rhs: Expression) : BinaryExpression(context, ".", lhs, rhs)
