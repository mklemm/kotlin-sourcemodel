package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.declaration.TypeParamProjection
import kotlin.reflect.KClass

class FunctionTypeUse : TypeUse() {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        super.reportUsedSymbols(c)
        parameterTypes.reportUsedSymbols(c)
        returnType.reportUsedSymbols(c)
        receiverType?.reportUsedSymbols(c)
    }

    var receiverType: TypeUse? = null
    var parameterTypes = mutableListOf<TypeUse>()
    var returnType: TypeUse = KClassUse.unit

    override fun generate(output: OutputContext) {
        if (isNullable) output.w("(")
        if (receiverType != null) {
            receiverType?.generate(output)
            output.w(".")
        }
        output.w("(")
        output.list(parameterTypes)
        output.w(") -> ")
        returnType.generate(output)
        if (isNullable) output.w(")?")
    }

    fun receiver(qualifiedName: QualifiedName, block: ClassTypeUse.() -> Unit) {
        receiverType = ClassTypeUse(qualifiedName).apply(block)
    }

    fun receiver(qualifiedName: String, block: ClassTypeUse.() -> Unit) {
        receiverType = ClassTypeUse(qualifiedName).apply(block)
    }

    fun receiver(type: ClassTypeUse, block: ClassTypeUse.() -> Unit = {}) {
        receiverType = type.copy().apply(block)
    }

    fun <T : Any> receiver(type: KClass<T>, block: KClassUse<T>.() -> Unit = {}) {
        receiverType = KClassUse(type).apply(block)
    }

    fun receiver(type: FunctionTypeUse) {
        receiverType = type
    }

    fun receiver(block: FunctionTypeUse.() -> Unit) {
        receiverType = FunctionTypeUse().apply(block)
    }

    fun receiver(name: String, projection: TypeParamProjection? = null, block: TypeParameterUse.() -> Unit) {
        receiverType = TypeParameterUse(name, projection).apply(block)
    }

    fun type(name: QualifiedName, block: ClassTypeUse.() -> Unit) {
        returnType = ClassTypeUse(name).apply(block)
    }

    fun type(name: String, block: ClassTypeUse.() -> Unit) {
        returnType = ClassTypeUse(name).apply(block)
    }

    fun type(type: ClassTypeUse, block: ClassTypeUse.() -> Unit = {}) {
        returnType = type.copy().apply(block)
    }

    fun type(type: FunctionTypeUse) {
        returnType = type
    }

    fun type(block: FunctionTypeUse.() -> Unit) {
        returnType = FunctionTypeUse().apply(block)
    }

    fun type(name: String, projection: TypeParamProjection? = null, block: TypeParameterUse.() -> Unit) {
        returnType = TypeParameterUse(name, projection).apply(block)
    }

    fun param(name: QualifiedName, block: ClassTypeUse.() -> Unit) {
        parameterTypes.add(ClassTypeUse(name).apply(block))
    }

    fun param(name: String, block: ClassTypeUse.() -> Unit) {
        parameterTypes.add(ClassTypeUse(name).apply(block))
    }

    fun param(block: FunctionTypeUse.() -> Unit) {
        parameterTypes.add(FunctionTypeUse().apply(block))
    }

    fun param(name: String, projection: TypeParamProjection? = null, block: TypeParameterUse.() -> Unit) {
        parameterTypes.add(TypeParameterUse(name, projection).apply(block))
    }

}
