package net.codesup.emit.declaration

import net.codesup.emit.expressions.Assign
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.use.*

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class ParameterDeclaration(sourceBuilder: SourceBuilder, name:String): TypedElementDeclaration(
    sourceBuilder,
    name
) {
    override val annotations = mutableListOf<AnnotationUse>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(type, init)
    var type: TypeUse = sourceBuilder.typeUse(sourceBuilder.unitType)
    var init: Assign? = null
    val modifiers = mutableListOf<ParameterModifier>()

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.q(name).w(": ")
        type.generate(scope, output)
        if(init != null) {
            output.w(" = ")
            init?.generate(scope, output)
        }
    }

    fun type(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        type = FunctionTypeUse(sourceBuilder, decl).apply(block)
    }

    fun type(block: InlineFunctionType.() -> Unit) {
        type = InlineFunctionType(sourceBuilder).apply(block)
    }

    fun type(typeUse: TypeUse) {
        type = typeUse
    }

    fun type(name: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        type = sourceBuilder.typeUse(name, block)
    }

    fun type(type: ClassDeclaration, block: ClassTypeUse.() -> Unit = {}) {
        this.type = sourceBuilder.typeUse(type, block)
    }

    fun type(type: FunctionTypeUse) {
        this.type = type
    }

    fun type(typeParam: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit = {}) {
        this.type = TypeParameterUse(sourceBuilder, typeParam).apply(block)
    }

    fun type(typeParamName: String, block: TypeParameterUse.() -> Unit = {}) {
        this.type = TypeParameterUse(sourceBuilder, TypeParameterDeclaration(sourceBuilder, typeParamName)).apply(block)
    }

    fun init(block: Assign.() -> Unit) {
        this.init = Assign(sourceBuilder, null).apply(block)
    }

    fun init(expression: Assign) {
        this.init = expression
    }

    fun modifier(vararg mod: ParameterModifier) = modifiers.addAll(mod.toList())
}

