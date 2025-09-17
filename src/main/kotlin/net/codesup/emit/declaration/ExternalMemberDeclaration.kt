package net.codesup.emit.declaration

import net.codesup.emit.*
import net.codesup.emit.use.TypeUse

open class ExternalMemberDeclaration(sourceBuilder: SourceBuilder, override val qualifiedName: QualifiedName): CallableDeclaration(sourceBuilder), Parameterized {
    override val name:String = qualifiedName.localPart
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

    }

}


