package net.codesup.emit.expressions

import net.codesup.emit.QualifiedName
import net.codesup.emit.declaration.*
import net.codesup.emit.use.ExternalTypeUse
import net.codesup.emit.use.KClassUse
import net.codesup.emit.use.TypeUse
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface ExpressionContext {

    fun v(name: String):Variable
    fun v(property: Declaration):PropertyVar
    fun Expression.v(name: String):Deref
    fun Expression.v(property: Declaration):Deref
    fun t(className: String):Variable
    fun <T:Any> t(kClass: KClass<T>):KClassUse<T>

    fun Expression.call(functionRef: KFunction<*>, vararg params: Expression):Deref = fn(functionRef.name, *params)
    fun Expression.call(namedDeclaration: Declaration, vararg params: Expression):Deref = fn(namedDeclaration.name, *params)

    fun Declaration.call(functionRef: KFunction<*>, vararg params: Expression):Deref = v(this).fn(functionRef.name, *params)
    fun Declaration.call(namedDeclaration: Declaration, vararg params: Expression):Deref = v(this).fn(namedDeclaration.name, *params)

    fun str(str: String):Str
    fun lit(any: Any):Lit

    fun call(typeDeclaration: TypeDeclaration, block: ConstructorInv.() -> Unit = {}): ConstructorInv
    fun <T:Any> call(typeDeclaration: KClassDeclaration<T>, block: ConstructorInv.() -> Unit = {}): ConstructorInv
    fun call(functionName: QualifiedName, block: Invocation.() -> Unit = {}): Invocation
    fun call(functionName: String, block: Invocation.() -> Unit = {}): Invocation
    fun Expression.fn(functionName: QualifiedName, vararg params: Expression): Deref
    fun Expression.fn(functionName: String, vararg params: Expression): Deref
    fun Declaration.fn(functionName: QualifiedName, vararg params: Expression): Deref = v(this).fn(functionName, *params)
    fun Declaration.fn(functionName: String, vararg params: Expression): Deref= v(this).fn(functionName, *params)
    fun call(functionDeclaration: CallableDeclaration, block: Invocation.() -> Unit = {}): Invocation
    fun call(typedElementDeclaration: TypedElementDeclaration, block: Invocation.() -> Unit = {}): Invocation
    fun <T:Any> call(kClass: KClass<T>, block: Invocation.() -> Unit = {}): Invocation
    fun _null(): Lit
    fun _this(): Lit
    fun _this(declaration: Declaration):Lit
    fun <T : Any> _ref(kClass: KClass<T>, block: KClassUse<T>.() -> Unit = {}):KClassUse<T>
    fun _ref(qName: String, block: ExternalTypeUse.() -> Unit = {}): ExternalTypeUse
    fun _ref(qName: QualifiedName, block: ExternalTypeUse.() -> Unit = {}): ExternalTypeUse

    infix fun Expression.ref(rhs: Expression): Deref

    operator fun Expression.plus(e: Expression): BinaryOperator
    operator fun Expression.minus(e: Expression): BinaryOperator
    operator fun Expression.times(e: Expression): BinaryOperator
    operator fun Expression.div(e: Expression): BinaryOperator

    operator fun Expression.plus(x: Number): BinaryOperator
    operator fun Expression.minus(x: Number): BinaryOperator
    operator fun Expression.times(x: Number): BinaryOperator
    operator fun Expression.div(x: Number): BinaryOperator

    operator fun Number.plus(e: Expression): BinaryOperator
    operator fun Char.plus(e: Expression): BinaryOperator

    operator fun Number.minus(e: Expression): BinaryOperator
    operator fun Char.minus(e: Expression): BinaryOperator

    operator fun Number.times(e: Expression): BinaryOperator
    operator fun Number.div(e: Expression): BinaryOperator


    fun Expression.cast(t: TypeUse): Cast
    fun Expression.nullableCast(t: TypeUse): NullableCast

    infix fun Expression.assign(e: Expression): BinaryExpression

}
