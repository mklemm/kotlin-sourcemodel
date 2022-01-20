package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.Parameterized
import net.codesup.emit.QualifiedName
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.FunctionTypeUse
import net.codesup.emit.use.TypeUse
import net.codesup.emit.use.Use

open class FunctionDeclaration(override val name:String): CallableDeclaration<FunctionDeclaration>(),
    NamedDeclaration<FunctionDeclaration>, Parameterized {
    override val parameters = mutableListOf<ParameterDeclaration>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        super<CallableDeclaration>.reportUsedSymbols(c)
        c.add(parameters, typeParameters)
        c.add(receiverType, returnType)
    }
    var receiverType: TypeUse? = null
    var returnType: TypeUse? = null
    val typeParameters = mutableListOf<TypeParamDeclaration>()

    override fun use(block: Use<FunctionDeclaration>.() -> Unit): Use<FunctionDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<FunctionDeclaration>.() -> Unit): Use<FunctionDeclaration> {
        TODO("Not yet implemented")
    }

    override fun generate(output: OutputContext) {
        output.g(doc)
        output.w(modifiers.joinToString(" "))
        output.w("fun ")
        output.list(typeParameters, ", ", "<", "> ")
        receiverType?.let { output.g(it).w(".") }
        output.q(name).w("(")
        output.list(parameters)
        output.w(")")
        if(returnType != null) {
            output.w(": ")
            returnType?.generate(output)
        }
        output.list(typeParameters.flatMap { it.fullBoundaries }, ", ", " where ")
        block?.generate(output)
    }

    fun receiver(typeName: QualifiedName, block: ClassTypeUse.() -> Unit) {
        this.receiverType = ClassTypeUse(typeName).apply(block)
    }
    fun receiver(typeName:String, block: ClassTypeUse.() -> Unit) {
        this.receiverType = ClassTypeUse(typeName).apply(block)
    }
    fun returnType(typeName: QualifiedName, block: ClassTypeUse.() -> Unit) {
        this.returnType = ClassTypeUse(typeName).apply(block)
    }
    fun returnType(typeName:String, block: ClassTypeUse.() -> Unit) {
        this.returnType = ClassTypeUse(typeName).apply(block)
    }
    fun returnType(block: FunctionTypeUse.() -> Unit) {
        this.returnType = FunctionTypeUse().apply(block)
    }
    fun returnType(typeUse: ClassTypeUse) {
        this.returnType = typeUse
    }
    fun typeParam(name:String, block: TypeParamDeclaration.() -> Unit) {
        typeParameters.add(TypeParamDeclaration(name).apply(block))
    }
}

