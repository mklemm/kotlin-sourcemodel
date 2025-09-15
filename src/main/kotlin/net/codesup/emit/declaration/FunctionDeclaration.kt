package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.Parameterized
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.ExternalTypeUse
import net.codesup.emit.use.FunctionTypeUse
import net.codesup.emit.use.TypeUse

open class FunctionDeclaration(sourceBuilder: SourceBuilder, override val name:String): CallableDeclaration(sourceBuilder), Parameterized {
    override val parameters: MutableList<ParameterDeclaration> = mutableListOf()
    override val doc: KDocBuilder = KDocBuilder()

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        c.add(parameters, typeParameters)
        c.add(receiverType, returnType)
    }
    var receiverType: TypeUse? = null
    var returnType: TypeUse? = null
    val typeParameters = mutableListOf<TypeParameterDeclaration>()

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.g(scope, doc)
        output.w(modifiers.joinToString(" "))
        output.w("fun ")
        output.list(scope, typeParameters, prefix = "<", suffix = "> ")
        receiverType?.let { output.g(scope, it).w(".") }
        output.q(name).w("(")
        output.list(scope, parameters)
        output.w(")")
        if(returnType != null) {
            output.w(": ")
            returnType?.generate(scope, output)
        }
        output.list(scope, typeParameters.flatMap { it.fullBoundaries }, prefix = " where ")
        block?.generate(scope, output)
    }

    fun receiver(type: ClassDeclaration, block: ClassTypeUse.() -> Unit) {
        this.receiverType = sourceBuilder.typeUse(type, block)
    }
    fun receiver(type: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit) {
        this.receiverType = sourceBuilder.typeUse(type, block)
    }
    fun receiver(qualifiedName: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        this.receiverType = sourceBuilder.typeUse(qualifiedName, block)
    }
    fun receiver(qualifiedName:String, block: ExternalTypeUse.() -> Unit) {
        this.receiverType = sourceBuilder.typeUse(qualifiedName, block)
    }
    fun type(type: ClassDeclaration, block: ClassTypeUse.() -> Unit) {
        this.returnType = sourceBuilder.typeUse(type, block)
    }
    fun type(qualifiedName: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        this.returnType = sourceBuilder.typeUse(qualifiedName, block)
    }
    fun type(typeName:String, block: ExternalTypeUse.() -> Unit) {
        this.returnType = sourceBuilder.typeUse(typeName, block)
    }
    fun type(functionTypeDeclaration: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        this.returnType = FunctionTypeUse(sourceBuilder, functionTypeDeclaration).apply(block)
    }
    fun type(typeUse: ClassTypeUse) {
        this.returnType = typeUse
    }
    fun typeParam(name:String, block: TypeParameterDeclaration.() -> Unit) {
        typeParameters.add(TypeParameterDeclaration(sourceBuilder, name).apply(block))
    }

    override fun pathTo(symbol: Symbol): Sequence<Symbol>? {
        return null
    }
}

