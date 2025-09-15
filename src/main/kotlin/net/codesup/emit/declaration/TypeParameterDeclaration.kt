package net.codesup.emit.declaration

import net.codesup.emit.Generable
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.sourceBuilder
import net.codesup.emit.use.*
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class TypeParameterDeclaration(sourceBuilder: SourceBuilder, name:String): TypeDeclaration(sourceBuilder, name) {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(
        annotations, boundaries
    )
    override val annotations = mutableListOf<AnnotationUse>()
    override val doc: KDocBuilder = KDocBuilder()
    var isReified: Boolean = false
    var variance: TypeParamVariance? = null
    val boundaries = mutableListOf<TypeUse>()

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        if(isReified) {
            output.w("reified ")
        }
        variance?.let{ it.generate(scope, output); output.w(" ") }
        output.q(name)
        if(boundaries.size == 1) {
            output.w(": ").g(scope, boundaries.first())
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

    fun <T:Any> bound(type: KClass<T>, block: KClassUse<T>.() -> Unit = {}) {
        boundaries.add(sourceBuilder.typeUse(type, block))
    }

    fun bound(className: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        boundaries.add(sourceBuilder.typeUse(className, block))
    }

    fun bound(className:String, block: ExternalTypeUse.() -> Unit) {
        boundaries.add(sourceBuilder.typeUse(className, block))
    }

    fun bound(classDeclaration: ClassDeclaration, block: ClassTypeUse.() -> Unit) {
        boundaries.add(sourceBuilder.typeUse(classDeclaration, block))
    }

    fun bound(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        boundaries.add(FunctionTypeUse(sourceBuilder, decl).apply(block))
    }

    fun bound(block: FunctionTypeDeclaration.() -> Unit) {
        boundaries.add(FunctionTypeUse(sourceBuilder, FunctionTypeDeclaration(sourceBuilder).apply(block)))
    }

    override fun pathTo(symbol: Symbol): Sequence<Symbol>? {
        return null
    }
}

class TypeParamBoundary(val name:String, val typeUse: TypeUse): Generable {
    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.q(name).w(": ")
        typeUse.generate(scope, output)
    }
}

enum class TypeParamProjection(val value:String): Generable {
    STAR("*"),
    IN("in"),
    OUT("out");

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w(value)
    }
}

enum class TypeParamVariance(val value:String): Generable {
    IN("in"),
    OUT("out");

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.w(value)
    }
}
