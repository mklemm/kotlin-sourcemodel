package net.codesup.emit.declaration

import net.codesup.emit.expressions.Assign
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.expressions.Expression
import net.codesup.emit.expressions.ExpressionContext
import net.codesup.emit.expressions.ExpressionFactory
import net.codesup.emit.expressions.Statement
import net.codesup.emit.use.*

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class PropertyDeclaration(sourceBuilder: SourceBuilder, name: String) : TypedElementDeclaration(
    sourceBuilder,
    name
) {
    override val annotations = mutableListOf<AnnotationUse>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        c.add(
            type,
            getter,
            setter,
            init
        )
        c.add(annotations)
    }
    var isMutable: Boolean = false
    var getter: GetterDeclaration? = null
    var setter: SetterDeclaration? = null
    var init: Statement? = null
    var type: TypeUse? = null
    val modifiers = mutableListOf<PropertyModifier>()

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        annotations.forEach {
            output.g(scope, it)
            output.wl()
        }
        output.join(modifiers.map {it.value}, " ", "", " ")
        output.w(if (isMutable) "var" else "val").w(" ").q(name)
        if (type != null) {
            output.w(": ")
            type?.generate(scope, output)
        }
        if(getter != null) {
            output.wl()
            getter?.generate(scope, output)
        }
        if(setter != null) {
            output.wl()
            setter?.generate(scope, output)
        }
        if(init != null) {
            output.w(" = ")
            init?.generate(scope, output)
            output.wl()
        }
    }

    fun type(decl: FunctionTypeDeclaration, block: FunctionTypeUse.() -> Unit) {
        type = FunctionTypeUse(sourceBuilder, decl).apply(block)
    }

    fun type(name: QualifiedName, block: ExternalTypeUse.() -> Unit) {
        type = sourceBuilder.typeUse(name, block)
    }

    fun type(type: ClassDeclaration, block: ClassTypeUse.() -> Unit = {}) {
        this.type = sourceBuilder.typeUse(type, block)
    }

    fun type(type: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit = {}) {
        this.type = sourceBuilder.typeUse(type, block)
    }

    fun <T:Any> type(type: kotlin.reflect.KClass<T>, block: KClassUse<T>.() -> Unit = {}) {
        this.type = sourceBuilder.typeUse(type, block)
    }

    fun type(type: TypeUse) {
        this.type = type
    }

    fun type(typeParamName:String, block: TypeParameterUse.() -> Unit = {}) {
        this.type = TypeParameterUse(sourceBuilder, TypeParameterDeclaration(sourceBuilder, typeParamName)).apply(block)
    }

    fun type(typeParam: TypeParameterDeclaration, block: TypeParameterUse.() -> Unit = {}) {
        this.type = TypeParameterUse(sourceBuilder, typeParam).apply(block)
    }

    fun init(block: ExpressionContext.() -> Unit) {
        this.init = ExpressionFactory(sourceBuilder).apply(block)
    }

    fun modifier(vararg mod: PropertyModifier) = modifiers.addAll(mod.toList())
    fun isLateInit() = modifier(PropertyModifier.LATEINIT)
}

