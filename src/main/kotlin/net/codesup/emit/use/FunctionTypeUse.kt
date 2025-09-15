package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.DeclarationScope
import net.codesup.emit.declaration.FunctionTypeDeclaration
import net.codesup.emit.declaration.TypeParamProjection
import kotlin.reflect.KClass

class FunctionTypeUse(sourceBuilder: SourceBuilder, declaration: FunctionTypeDeclaration) : TypeUse(sourceBuilder, declaration) {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        super.reportUsedSymbols(c)
        parameterTypes.reportUsedSymbols(c)
        returnType.reportUsedSymbols(c)
        receiverType?.reportUsedSymbols(c)
    }

    var receiverType: TypeUse? = null
    var parameterTypes = mutableListOf<TypeUse>()
    var returnType: TypeUse = sourceBuilder.typeUse(sourceBuilder.unitType)

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        if (isNullable) output.w("(")
        declaration.generate(scope, output)
        if (isNullable) output.w(")?")
    }
}
