package net.codesup.emit.use

import net.codesup.emit.*
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.ExternalTypeDeclaration
import net.codesup.emit.declaration.FunctionTypeDeclaration
import net.codesup.emit.declaration.KClassDeclaration
import net.codesup.emit.declaration.TypeParameterDeclaration
import net.codesup.emit.expressions.DotClass
import net.codesup.emit.expressions.ExpressionContext
import kotlin.reflect.KClass

open class ExternalTypeUse(sourceBuilder: SourceBuilder, declaration: ExternalTypeDeclaration) : TypeUse(sourceBuilder, declaration), Use {

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        c.add(declaration)
        c.add(typeArguments)
    }

    val typeArguments = mutableListOf<TypeUse>()

    fun arg(typeParamName: String, block: TypeParameterUse.() -> Unit = {}) = arg(TypeParameterDeclaration(sourceBuilder, typeParamName), block)

    fun arg(typeParam: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit = {}) {
        typeArguments.add(TypeParameterUse(sourceBuilder, typeParam).apply(block))
    }

    fun arg(type: ExternalTypeUse, block: ExternalTypeUse.() -> Unit = {}) {
        typeArguments.add(type.copy().apply(block))
    }

    fun arg(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        typeArguments.add(FunctionTypeUse(sourceBuilder, decl).apply(block))
    }

    fun arg(typeArg: TypeUse) {
        typeArguments.add(typeArg)
    }

    fun <T : Any> arg(kClass: KClass<T>, block: KClassUse<T>.() -> Unit) {
        typeArguments.add(KClassUse<T>(sourceBuilder, KClassDeclaration(sourceBuilder, kClass)).apply(block))
    }

    fun dotClass() = DotClass(sourceBuilder, this)

    val isParameterized: Boolean get() = typeArguments.isNotEmpty()

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        annotations.forEach { anno ->
            output.g(scope, anno).w(" ")
        }
        output.w((declaration as ExternalTypeDeclaration).qualifiedName)
        if (isNullable) output.w("?")
        output.list(scope, typeArguments, prefix = "<", suffix = ">")
    }

    open fun copy(newDeclaration: ExternalTypeDeclaration = (declaration as ExternalTypeDeclaration), block: ExternalTypeUse.() -> Unit = {}) =
        ExternalTypeUse(sourceBuilder, newDeclaration).apply {
            this.isNullable = this@ExternalTypeUse.isNullable
            this.annotations.addAll(this@ExternalTypeUse.annotations)
            this.typeArguments.addAll(this@ExternalTypeUse.typeArguments)
            block()
        }
}
