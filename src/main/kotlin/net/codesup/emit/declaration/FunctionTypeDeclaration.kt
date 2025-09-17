package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.ExternalTypeUse
import net.codesup.emit.use.FunctionTypeUse
import net.codesup.emit.use.InlineFunctionType
import net.codesup.emit.use.KClassUse
import net.codesup.emit.use.TypeParameterUse
import net.codesup.emit.use.TypeUse
import kotlin.reflect.KClass

class FunctionTypeDeclaration(sourceBuilder: SourceBuilder) : TypeDeclaration(sourceBuilder, ""), FunctionTypeSupport {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        parameterTypes.reportUsedSymbols(c)
        returnType.reportUsedSymbols(c)
        receiverType?.reportUsedSymbols(c)
    }

    var receiverType: TypeUse? = null
    var parameterTypes = mutableListOf<TypeUse>()
    var returnType: TypeUse = sourceBuilder.typeUse(sourceBuilder.unitType)

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        if (receiverType != null) {
            receiverType?.generate(scope, output)
            output.w(".")
        }
        output.w("(")
        output.list(scope, parameterTypes)
        output.w(") -> ")
        returnType.generate(scope, output)
    }

    override fun receiver(qualifiedName: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        receiverType = sourceBuilder.typeUse(qualifiedName, block)
    }

    override fun receiver(type: ClassDeclaration, block: ClassTypeUse.() -> Unit) {
        receiverType = sourceBuilder.typeUse(type, block)
    }

    override fun receiver(
        type: ExternalTypeDeclaration,
        block: ExternalTypeUse.() -> Unit
    ) {
        receiverType = sourceBuilder.typeUse(type, block)
    }

    override fun receiver(
        decl: TypeParameterDeclaration,
        block: TypeParameterUse.() -> Unit
    ) {
        receiverType = TypeParameterUse(sourceBuilder, decl).apply(block)
    }

    override fun <T : Any> receiver(type: KClass<T>, block: KClassUse<T>.() -> Unit) {
        receiverType = sourceBuilder.typeUse(type, block)
    }

    override fun receiver(typeUse: TypeUse) {
        receiverType = typeUse
    }

    override fun receiver(functionTypeDeclaration: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        receiverType = FunctionTypeUse(sourceBuilder, functionTypeDeclaration).apply(block)
    }

    override fun receiver(block: InlineFunctionType.() -> Unit) {
        receiverType = InlineFunctionType(sourceBuilder).apply(block)
    }

    override fun receiver(
        typeParamName: String,
        block: TypeParameterUse.() -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun type(qualifiedName: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        returnType = sourceBuilder.typeUse(qualifiedName, block)
    }

    override fun type(type: ClassDeclaration, block: ClassTypeUse.() -> Unit) {
        returnType = sourceBuilder.typeUse(type, block)
    }
    override fun type(type: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit) {
        returnType = sourceBuilder.typeUse(type, block)
    }

    override fun type(type: TypeUse) {
        returnType = type
    }

    override fun type(functionTypeDeclaration: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        returnType = FunctionTypeUse(sourceBuilder, functionTypeDeclaration).apply(block)
    }

    override fun type(block: InlineFunctionType.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun type(typeParamName: String, block: TypeParameterUse.() -> Unit) = type(TypeParameterDeclaration(sourceBuilder, typeParamName), block)

    override fun type(decl: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit) {
        returnType = TypeParameterUse(sourceBuilder, decl).apply(block)
    }

    override fun <T : Any> type(
        type: KClass<T>,
        block: KClassUse<T>.() -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun param(name: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        parameterTypes.add(sourceBuilder.typeUse(name, block))
    }

    override fun param(declaration: ClassDeclaration, block: ClassTypeUse.() -> Unit) {
        parameterTypes.add(sourceBuilder.typeUse(declaration, block))
    }

    override fun param(typeUse: TypeUse) {
        parameterTypes.add(typeUse)
    }

    override fun param(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        parameterTypes.add(FunctionTypeUse(sourceBuilder, decl).apply(block))
    }

    override fun param(typeParam: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit) {
        parameterTypes.add(TypeParameterUse(sourceBuilder, typeParam).apply(block))
    }

    override fun param(name: String, block: TypeParameterUse.() -> Unit) = param(TypeParameterDeclaration(sourceBuilder, name), block)

    override val doc: KDocBuilder = KDocBuilder()

}
