package net.codesup.util.emit.use

import net.codesup.util.emit.*
import net.codesup.util.emit.declaration.ClassDeclaration
import net.codesup.util.emit.declaration.TypeParamProjection
import kotlin.reflect.KClass

open class ClassTypeUse(override val qualifiedName: QualifiedName) : TypeUse(), Qualifiable, Use<ClassDeclaration>, ExpressionContext {
    constructor(vararg qName: String) : this(QualifiedName(*qName))

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        super.reportUsedSymbols(c)
        c.add(qualifiedName)
        c.add(typeArguments)
    }

    val typeArguments = mutableListOf<TypeUse>()

    fun arg(typeName: String, block: ClassTypeUse.() -> Unit) {
        typeArguments.add(ClassTypeUse(typeName).apply(block))
    }

    fun arg(typeName: QualifiedName, block: ClassTypeUse.() -> Unit = {}) {
        typeArguments.add(ClassTypeUse(typeName).apply(block))
    }

    fun arg(typeParamName: String, projection: TypeParamProjection? = null, block: TypeParameterUse.() -> Unit = {}) {
        typeArguments.add(TypeParameterUse(typeParamName, projection).apply(block))
    }

    fun arg(type: ClassTypeUse, block: ClassTypeUse.() -> Unit = {}) {
        typeArguments.add(type.copy().apply(block))
    }

    fun arg(block: FunctionTypeUse.() -> Unit) {
        typeArguments.add(FunctionTypeUse().apply(block))
    }

    fun arg(typeArg: TypeUse) {
        typeArguments.add(typeArg)
    }

    fun <T : Any> arg(kClass: KClass<T>, block: KClassUse<T>.() -> Unit) {
        typeArguments.add(KClassUse<T>(kClass).apply(block))
    }

    override var top: Expression? = null

    fun dotClass() = DotClass(this, this)
    fun ref(e: Expression) = Deref(this, this, e)
    fun _new(block: ConstructorInv.() -> Unit) = ConstructorInv(this, this).apply(block).also { top = it }

    val isParameterized:Boolean get() = typeArguments.isNotEmpty()

    override fun generate(output: OutputContext) {
        annotations.forEach { anno ->
            output.g(anno).w(" ")
        }
        output.w(qualifiedName)
        if (isNullable) output.w("?")
        output.list(typeArguments, ", ", "<", ">")
    }

    open fun copy(qualifiedName: QualifiedName = this.qualifiedName, block: ClassTypeUse.() -> Unit = {}) =
        ClassTypeUse(qualifiedName).apply {
            this.isNullable = this@ClassTypeUse.isNullable
            this.annotations.addAll(this@ClassTypeUse.annotations)
            this.typeArguments.addAll(this@ClassTypeUse.typeArguments)
            block()
        }
}
