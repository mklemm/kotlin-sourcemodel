package net.codesup.emit.use

import net.codesup.emit.OutputContext
import net.codesup.emit.declaration.TypeParamProjection

class TypeParameterUse(val name: String, val projection: TypeParamProjection? = null) : TypeUse() {
    companion object {
        val star = TypeParameterUse("*", null)
    }

    override fun generate(output: OutputContext) {
        if (projection != null && projection != TypeParamProjection.STAR) {
            output.w(projection.value)
        }
        output.q(name)
        if(isNullable) output.w("?")
    }
}
