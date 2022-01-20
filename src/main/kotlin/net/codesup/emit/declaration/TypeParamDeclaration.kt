package net.codesup.util.emit.declaration

import net.codesup.util.emit.*
import net.codesup.util.emit.use.*
import net.codesup.util.emit.use.TypeUse

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class TypeParamDeclaration(override val name:String): NamedDeclaration<TypeParamDeclaration> {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(
        annotations, boundaries
    )
    override val annotations = mutableListOf<AnnotationUse>()
    override val doc: KDocBuilder = KDocBuilder()
    var isReified: Boolean = false
    var variance: TypeParamVariance? = null
    val boundaries = mutableListOf<TypeUse>()

    override fun use(block: Use<TypeParamDeclaration>.() -> Unit): Use<TypeParamDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<TypeParamDeclaration>.() -> Unit): Use<TypeParamDeclaration> {
        TODO("Not yet implemented")
    }

    override fun generate(output: OutputContext) {
        if(isReified) {
            output.w("reified ")
        }
        variance?.let{ it.generate(output); output.w(" ") }
        output.q(name)
        if(boundaries.size == 1) {
            output.w(": ").g(boundaries.first())
        }
    }

    val fullBoundaries get() =
        if(boundaries.size > 1) {
            boundaries.map { TypeParamBoundary(name, it) }
        } else {
            listOf()
        }


    fun bound(bound: TypeUse) {
        boundaries.add(bound)
    }

    fun bound(className: QualifiedName, block: ClassTypeUse.() -> Unit) {
        boundaries.add(ClassTypeUse(className).apply(block))
    }

    fun bound(className:String, block: ClassTypeUse.() -> Unit) {
        boundaries.add(ClassTypeUse(className).apply(block))
    }

    fun bound(block: FunctionTypeUse.() -> Unit) {
        boundaries.add(FunctionTypeUse().apply(block))
    }

    override val declarations = mutableListOf<Declaration<*>>()
}

class TypeParamBoundary(val name:String, val typeUse: TypeUse): Generable {
    override fun generate(output: OutputContext) {
        output.q(name).w(": ")
        typeUse.generate(output)
    }
}

enum class TypeParamProjection(val value:String): Generable {
    STAR("*"),
    IN("in"),
    OUT("out");

    override fun generate(output: OutputContext) {
        output.w(value)
    }
}

enum class TypeParamVariance(val value:String): Generable {
    IN("in"),
    OUT("out");

    override fun generate(output: OutputContext) {
        output.w(value)
    }
}
