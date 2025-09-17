package net.codesup.emit.declaration

import net.codesup.emit.QualifiedName
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.ExternalTypeUse
import net.codesup.emit.use.FunctionTypeUse
import net.codesup.emit.use.InlineFunctionType
import net.codesup.emit.use.KClassUse
import net.codesup.emit.use.TypeParameterUse
import net.codesup.emit.use.TypeUse
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2025-09-12
 *
 */
interface FunctionTypeSupport {
    fun receiver(typeUse: TypeUse)
    fun receiver(qualifiedName: QualifiedName, block: ExternalTypeUse.() -> Unit = {})
    fun receiver(type: ClassDeclaration, block: ClassTypeUse.() -> Unit = {})
    fun receiver(type: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit = {})
    fun receiver(decl: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit = {})
    fun <T : Any> receiver(type: KClass<T>, block: KClassUse<T>.() -> Unit = {})
    fun receiver(functionTypeDeclaration: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit)
    fun receiver(block: InlineFunctionType.() -> Unit)
    fun receiver(typeParamName: String, block: TypeParameterUse.() -> Unit)

    fun type(type: TypeUse)
    fun type(qualifiedName: QualifiedName, block: ExternalTypeUse.() -> Unit)
    fun type(type: ClassDeclaration, block: ClassTypeUse.() -> Unit = {})
    fun type(type: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit = {})
    fun type(decl: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit)
    fun <T : Any> type(type: KClass<T>, block: KClassUse<T>.() -> Unit = {})
    fun type(functionTypeDeclaration: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit)
    fun type(block: InlineFunctionType.() -> Unit)
    fun type(typeParamName: String, block: TypeParameterUse.() -> Unit)

    fun param(name: QualifiedName, block: ExternalTypeUse.() -> Unit)
    fun param(declaration: ClassDeclaration, block: ClassTypeUse.() -> Unit)
    fun param(typeUse: TypeUse)
    fun param(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit)
    fun param(typeParam: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit)
    fun param(name: String, block: TypeParameterUse.() -> Unit)
}
