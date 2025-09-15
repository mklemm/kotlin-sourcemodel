/**
 * @author Mirko Klemm 2025-09-15
 *
 */
package net.codesup.emit.expressions

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.*
import net.codesup.emit.sourceBuilder
import net.codesup.emit.use.ExternalTypeUse
import net.codesup.emit.use.KClassUse
import net.codesup.emit.use.TypeUse
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.instanceParameter

/**
 * @author Mirko Klemm 2025-09-12
 *
 */
open class ExpressionFactory(val sourceBuilder: SourceBuilder) : ExpressionContext, Statement {
    var expression: Expression? = null
    private fun <E : Expression> setExpression(e: E): E {
        this.expression = e
        return e
    }

    override fun v(name: String) = setExpression(Variable(sourceBuilder, name))
    override fun v(property: TypedElementDeclaration) = setExpression(PropertyVar(sourceBuilder, property))
    override fun Expression.v(name: String): Deref = createDeref {
        Variable(sourceBuilder, name)
    }

    override fun Expression.v(property: TypedElementDeclaration): Deref = createDeref {
        PropertyVar(sourceBuilder, property)
    }

    override fun t(name: String) = setExpression(Variable(sourceBuilder, name))
    override fun <T : Any> t(kClass: KClass<T>) =
        setExpression(KClassUse<T>(sourceBuilder, sourceBuilder.externalType(kClass)))
    override fun str(str: String) = setExpression(Str(sourceBuilder, str))
    override fun lit(any: Any) = setExpression(Lit(sourceBuilder, any))

    override fun call(typeDeclaration: TypeDeclaration, block: ConstructorInv.() -> Unit) =
        setExpression(ConstructorInv(sourceBuilder, typeDeclaration).apply(block))

    override fun <T : Any> call(typeDeclaration: KClassDeclaration<T>, block: ConstructorInv.() -> Unit) =
        setExpression(ConstructorInv(sourceBuilder, typeDeclaration).apply(block))

    override fun call(functionName: QualifiedName, block: Invocation.() -> Unit) =
        setExpression(Invocation(sourceBuilder, sourceBuilder.externalFunction(functionName)).apply(block))

    override fun call(functionName: String, block: Invocation.() -> Unit) =
        setExpression(Invocation(sourceBuilder, sourceBuilder.externalFunction(functionName)).apply(block))

    override fun Expression.call(functionName: QualifiedName, vararg params: Expression): Deref = createDeref {
        Invocation(sourceBuilder, sourceBuilder.externalFunction(functionName)).apply {
            invoke(*params)
        }
    }

    override fun Expression.call(functionName: String, vararg params: Expression): Deref =
        call(QualifiedName(functionName), *params)

    override fun call(functionDeclaration: FunctionDeclaration, block: Invocation.() -> Unit) =
        setExpression(Invocation(sourceBuilder, functionDeclaration).apply(block))

    override fun call(typedElementDeclaration: TypedElementDeclaration, block: Invocation.() -> Unit) =
        setExpression(Invocation(sourceBuilder, typedElementDeclaration).apply(block))

    override fun <T : Any> call(kClass: KClass<T>, block: Invocation.() -> Unit): Invocation =
        call(sourceBuilder.externalType(kClass), block)

    override fun _null() = lit("null")
    override fun _this() = lit("this")
    override fun _this(declaration: Declaration) = lit("this@${declaration.name}")
    override fun <T : Any> _ref(kClass: KClass<T>, block: KClassUse<T>.() -> Unit) = setExpression(
        KClassUse<T>(
            sourceBuilder, KClassDeclaration(
                sourceBuilder,
                kClass
            )
        ).apply(block)
    )

    override fun _ref(qName: String, block: ExternalTypeUse.() -> Unit) = setExpression(
        ExternalTypeUse(
            sourceBuilder, ExternalTypeDeclaration(
                sourceBuilder,
                qName
            )
        ).apply(block)
    )

    override fun _ref(qName: QualifiedName, block: ExternalTypeUse.() -> Unit) = setExpression(
        ExternalTypeUse(
            sourceBuilder, ExternalTypeDeclaration(
                sourceBuilder,
                qName
            )
        ).apply(block)
    )

    fun op(token: String, vararg operands: Expression) =
        setExpression(NAryOperator(sourceBuilder, token, operands.toList()))

    fun op(lhs: Expression, token: String, rhs: Expression) =
        setExpression(BinaryOperator(sourceBuilder, token, lhs, rhs))

    private fun <D : Expression> Expression.createDeref(factory: () -> D): Deref =
        setExpression(
            Deref(
                sourceBuilder,
                this,
                factory()
            )
        )


    private fun opLit(lhs: Expression, token: String, rhs: Any) =
        op(lhs, token, lit(rhs))

    private fun litOp(lhs: Any, token: String, rhs: Expression) =
        op(lit(lhs), token, rhs)

    override infix fun Expression.ref(rhs: Expression) = setExpression(Deref(sourceBuilder, this, rhs))

    override operator fun Expression.plus(e: Expression) = op(this, "+", e)
    override operator fun Expression.minus(e: Expression) = op(this, "-", e)
    override operator fun Expression.times(e: Expression) = op(this, "*", e)
    override operator fun Expression.div(e: Expression) = op(this, "/", e)

    override operator fun Expression.plus(x: Number) = opLit(this, "+", x)
    override operator fun Expression.minus(x: Number) = opLit(this, "-", x)
    override operator fun Expression.times(x: Number) = opLit(this, "*", x)
    override operator fun Expression.div(x: Number) = opLit(this, "/", x)

    override operator fun Number.plus(e: Expression) = litOp(this, "+", e)
    override operator fun Char.plus(e: Expression) = litOp(this, "+", e)

    override operator fun Number.minus(e: Expression) = litOp(this, "-", e)
    override operator fun Char.minus(e: Expression) = litOp(this, "-", e)

    override operator fun Number.times(e: Expression) = litOp(this, "*", e)
    override operator fun Number.div(e: Expression) = litOp(this, "/", e)


    override fun Expression.cast(t: TypeUse) = setExpression(Cast(sourceBuilder, this, t))
    override fun Expression.nullableCast(t: TypeUse) = setExpression(NullableCast(sourceBuilder, this, t))

    override infix fun Expression.assign(e: Expression): BinaryExpression =
        setExpression(BinaryExpression(sourceBuilder, " = ", this, e))

    override fun generate(
        scope: DeclarationScope,
        output: OutputContext
    ) {
        expression?.generate(scope, output)
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        expression?.reportUsedSymbols(c)
    }
}
