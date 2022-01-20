package net.codesup.emit

import net.codesup.emit.Qualifiable
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse
import net.codesup.emit.use.SymbolUser
import net.codesup.emit.use.TypeUse
import net.codesup.emit.Generable
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface ExpressionContext {
    var top: Expression?

    fun oper(token: String, vararg e: Expression) = Op(this, token, *e).also { top = it }
    fun v(name: String) = Var(this, name).also { top = it }
    fun inv(functionName: QualifiedName, block: Inv.() -> Unit) = Inv(this, functionName).apply(block).also { top = it }
    fun inv(functionName: String, block: Inv.() -> Unit) = Inv(this, QualifiedName(functionName)).apply(block).also { top = it }
    fun str(str: String) = Str(this, str).also { top = it }
    fun lit(any: Any) = Lit(this, any).also { top = it }
    fun _null() = lit("null")
    fun <T:Any> _ref(kClass:KClass<T>, block: KClassUse<T>.() -> Unit = {}) = KClassUse<T>(kClass).apply(block).also { top = it }
    fun _ref(qName:String, block: ClassTypeUse.() -> Unit = {}) = ClassTypeUse(qName).apply(block).also { top = it }
    fun _ref(qName: QualifiedName, block: ClassTypeUse.() -> Unit = {}) = ClassTypeUse(qName).apply(block).also { top = it }
    fun array(vararg e: Expression) = ListExpr(this, ", ", "[", "]", *e).also { top = it }
    fun array(block: ListExpr.() -> Unit) = ListExpr(this, ", ", "[", "]").apply(block).also { top = it }
    fun cast(t: TypeUse, e: Expression) = Cast(this, t, e)
}

interface Expression : Generable, SymbolUser {

}

interface ContainedExpression : Expression {
    val context: ExpressionContext

    fun op(token: String, vararg e: Expression) = context.oper(token, *e)

    private fun opLit(token: String, e: Expression, n: Any) =
        op(token, e, Lit(context, n))

    private fun litOp(token: String, n: Any, e: Expression) =
        op(token, Lit(context, n), e)

    operator fun plus(e: Expression) = op("+", this, e)
    operator fun minus(e: Expression) = op("-", this, e)
    operator fun times(e: Expression) = op("*", this, e)
    operator fun div(e: Expression) = op("/", this, e)

    operator fun plus(x: Number) = opLit("+", this, x)
    operator fun minus(x: Number) = opLit("-", this, x)
    operator fun times(x: Number) = opLit("*", this, x)
    operator fun div(x: Number) = opLit("/", this, x)

    operator fun Number.plus(e: Expression) = litOp("+", this, e)
    operator fun Char.plus(e: Expression) = litOp("+", this, e)

    operator fun Number.minus(e: Expression) = litOp("-", this, e)
    operator fun Char.minus(e: Expression) = litOp("-", this, e)

    operator fun Number.times(e: Expression) = litOp("*", this, e)
    operator fun Number.div(e: Expression) = litOp("/", this, e)

    fun cast(t: TypeUse) = Cast(context, t, this)
}

interface ContainerExpression : ContainedExpression {
    val children: MutableList<Expression>
}

abstract class SingleExpr(override val context: ExpressionContext) : ContainedExpression

abstract class Expr(override val context: ExpressionContext, vararg expressions: Expression) : ContainerExpression {
    override val children: MutableList<Expression> = expressions.toMutableList()
    override fun generate(output: OutputContext) {
        children.forEach {
            output.g(it)
        }
    }
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = children.reportUsedSymbols(c)
}

class ListExpr(context: ExpressionContext, val sep:String, val prefix: String, val suffix: String, vararg expressions: Expression):
    Expr(context, *expressions) {
    override fun generate(output: OutputContext) {
        output.list(children, sep, prefix, suffix)
    }

    fun el(e: Expression) = children.add(e)
}

class Var(context: ExpressionContext, val name: String) : SingleExpr(context) {
    override fun generate(output: OutputContext) {
        output.q(name)
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {}

    fun assign(expression: Expression) = NoParensOp(context, "=", this, expression)
}

class Str(context: ExpressionContext, val content: String) : SingleExpr(context) {
    override fun generate(output: OutputContext) {
        output.w("\"").w(content).w("\"")
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {}
}

class Inv(context: ExpressionContext, override val qualifiedName: QualifiedName) : SingleExpr(context), Qualifiable {
    val args = mutableListOf<Expression>()
    val typeArgs = mutableListOf<TypeUse>()

    fun arg(value: Expression): Inv {
        args.add(value)
        return this
    }

    fun typeArg(typeUse: TypeUse): Inv {
        typeArgs.add(typeUse)
        return this
    }

    override fun generate(output: OutputContext) {
        output.w(qualifiedName).list(typeArgs, ", ", "<", ">").w("(").list(args).w(")")
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(args, typeArgs)
}

class Deref(context: ExpressionContext, typeUse: TypeUse, expression: Expression) : Expr(context, typeUse, expression) {
    override fun generate(output: OutputContext) {
        output.g(children[0]).w(".").g(children[1])
    }
}

class Cast(context: ExpressionContext, typeUse: TypeUse, expression: Expression) : Expr(context, typeUse, expression) {
    override fun generate(output: OutputContext) {
        output.g(children[1]).w(" as ").g(children[0])
    }
}

class ConstructorInv(context: ExpressionContext, val classTypeUse: ClassTypeUse) : SingleExpr(context) {
    val args = mutableListOf<Expression>()

    fun arg(value: Expression): ConstructorInv {
        args.add(value)
        return this
    }

    fun arg(name: String, value: Expression): ConstructorInv {
        args.add(context.v(name).assign(value))
        return this
    }


    override fun generate(output: OutputContext) {
        classTypeUse.generate(output)
        output.w("(").list(args).w(")")
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        args.reportUsedSymbols(c)
        classTypeUse.reportUsedSymbols(c)
    }
}

class Op(context: ExpressionContext, val token: String, vararg expressions: Expression) : Expr(context, *expressions) {
    override fun generate(output: OutputContext) {
        output.list(children, " $token ", "(", ")")
    }
}

class NoParensOp(context: ExpressionContext, val token: String, vararg expressions: Expression) : Expr(context, *expressions) {
    override fun generate(output: OutputContext) {
        output.list(children, " $token ")
    }
}

class Assign(override var top: Expression? = null) : ContainedExpression, ExpressionContext {
    override val context = this
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        top?.reportUsedSymbols(c)
    }

    override fun generate(output: OutputContext) {
        output.w(" = ").g(top)
    }
}


class PrefixOp(context: ExpressionContext, val token: String, expression: Expression) : Expr(context, expression) {
    override fun generate(output: OutputContext) {
        output.w(" $token").g(children.first())
    }
}

class SuffixOp(context: ExpressionContext, val token: String, expression: Expression) : Expr(context, expression) {
    override fun generate(output: OutputContext) {
        output.g(children.first()).w("$token ")
    }
}

class Lit(context: ExpressionContext, val a: Any) : SingleExpr(context) {
    override fun generate(output: OutputContext) {
        output.w(a.toString())
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {}
}

class DotClass(context: ExpressionContext, val classTypeUse: ClassTypeUse) : SingleExpr(context) {
    override fun generate(output: OutputContext) {
        classTypeUse.generate(output)
        output.w("::class")
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = classTypeUse.reportUsedSymbols(c)

}
