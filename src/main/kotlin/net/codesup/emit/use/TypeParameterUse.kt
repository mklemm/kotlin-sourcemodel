package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.declaration.TypeParamProjection
import net.codesup.emit.declaration.TypeParameterDeclaration

class TypeParameterUse(sourceBuilder: SourceBuilder, declaration: TypeParameterDeclaration) : TypeUse(sourceBuilder, declaration) {
    var projection: TypeParamProjection? = null
        private set

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        if (projection != null && projection != TypeParamProjection.STAR) {
            output.w(projection!!.value)
        }
        output.q(declaration.name)
        if(isNullable) output.w("?")
    }

    fun projection(projection: TypeParamProjection){
        this.projection = projection
    }

}
