package net.codesup.util.emit.use

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.DeclarationOwner
import net.codesup.util.emit.declaration.TypeParamProjection
import net.codesup.util.emit.declaration.TypeParameterDeclaration

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
