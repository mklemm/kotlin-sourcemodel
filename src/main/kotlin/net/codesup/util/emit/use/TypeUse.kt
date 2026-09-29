package net.codesup.util.emit.use

import net.codesup.util.emit.Annotatable
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.declaration.ClassDeclaration
import net.codesup.util.emit.declaration.ExternalTypeDeclaration
import net.codesup.util.emit.declaration.FunctionTypeDeclaration
import net.codesup.util.emit.declaration.KClassDeclaration
import net.codesup.util.emit.declaration.TypeDeclaration
import net.codesup.util.emit.declaration.TypeParameterDeclaration
import net.codesup.util.emit.expressions.DotClass
import net.codesup.util.emit.expressions.SingleExpr
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
abstract class TypeUse(sourceBuilder: SourceBuilder, val declaration: TypeDeclaration) : SingleExpr(sourceBuilder), Annotatable, SymbolUser, Use {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        annotations.reportUsedSymbols(c)
    }
    override val annotations = mutableListOf<AnnotationUse>()
    var isNullable: Boolean = false

    val typeArguments = mutableListOf<TypeUse>()

    fun arg(typeParamName: String, block: TypeParameterUse.() -> Unit = {}) {
        typeArguments.add(TypeParameterUse(sourceBuilder, TypeParameterDeclaration(sourceBuilder, typeParamName)).apply(block))
    }

    fun arg(typeParam: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit = {}) {
        typeArguments.add(TypeParameterUse(sourceBuilder, typeParam).apply(block))
    }

    fun arg(type: ClassDeclaration, block: ClassTypeUse.() -> Unit = {}) {
        typeArguments.add(sourceBuilder.typeUse(type, block))
    }

    fun arg(type: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit = {}) {
        typeArguments.add(sourceBuilder.typeUse(type, block))
    }

    fun arg(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        typeArguments.add(FunctionTypeUse(sourceBuilder, decl).apply(block))
    }

    fun arg(typeArg: TypeUse) {
        typeArguments.add(typeArg)
    }

    fun <T : Any> arg(kClass: KClass<T>, block: KClassUse<T>.() -> Unit = {}) {
        typeArguments.add(KClassUse<T>(sourceBuilder, KClassDeclaration(sourceBuilder, kClass)).apply(block))
    }

    fun dotClass() = DotClass(sourceBuilder,this)

    val isParameterized:Boolean get() = typeArguments.isNotEmpty()


}

