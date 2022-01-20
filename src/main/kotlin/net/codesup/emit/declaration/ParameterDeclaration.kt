package net.codesup.emit.declaration

import net.codesup.emit.Assign
import net.codesup.emit.Expression
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.use.*

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class ParameterDeclaration(override val name:String): PrimaryConstructorParameterDeclaration<ParameterDeclaration> {
    override val annotations = mutableListOf<AnnotationUse>()
    override val declarations = mutableListOf<Declaration<*>>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(type, init)
    var type: TypeUse = KClassUse.unit
    var init: Expression? = null
    val modifiers = mutableListOf<ParameterModifier>()

    override fun use(block: Use<ParameterDeclaration>.() -> Unit): ParameterUse =
        ParameterUse(this).apply(block)


    override fun ref(block: Use<ParameterDeclaration>.() -> Unit): ParameterUse =
            ParameterUse(this).apply(block)

    override fun generate(output: OutputContext) {
        output.q(name).w(": ")
        type.generate(output)
        if(init != null) {
            output.w(" = ")
            init?.generate(output)
        }
    }

    fun type(block: FunctionTypeUse.() -> Unit) {
        type = FunctionTypeUse().apply(block)
    }

    fun type(name: QualifiedName, block: ClassTypeUse.() -> Unit) {
        type = ClassTypeUse(name).apply(block)
    }

    fun type(name:String, block: ClassTypeUse.() -> Unit) {
        type = ClassTypeUse(name).apply(block)
    }

    fun type(type: ClassTypeUse, block: ClassTypeUse.() -> Unit = {}) {
        this.type = type.copy(type.qualifiedName).apply(block)
    }

    fun type(type: FunctionTypeUse) {
        this.type = type
    }

    fun type(typeParamName:String, projection: TypeParamProjection? = null, block: TypeParameterUse.() -> Unit = {}) {
        this.type = TypeParameterUse(typeParamName, projection).apply(block)
    }

    fun init(block: Assign.() -> Unit) {
        this.init = Assign().apply(block)
    }

    fun init(expression: Assign) {
        this.init = expression
    }

    fun modifier(vararg mod: ParameterModifier) = modifiers.addAll(mod.toList())
}

