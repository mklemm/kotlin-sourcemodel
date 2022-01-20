package net.codesup.emit.declaration

import net.codesup.emit.Assign
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.use.*

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class PropertyDeclaration(override val name: String) : PrimaryConstructorParameterDeclaration<PropertyDeclaration> {
    override val annotations = mutableListOf<AnnotationUse>()
    override val declarations = mutableListOf<Declaration<*>>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        c.add(
            type,
            getter,
            setter,
            init
        )
        c.add(annotations, declarations)
    }
    var isMutable: Boolean = false
    var getter: GetterDeclaration? = null
    var setter: SetterDeclaration? = null
    var init: Assign? = null
    var type: TypeUse? = null
    val modifiers = mutableListOf<PropertyModifier>()

    override fun use(block: Use<PropertyDeclaration>.() -> Unit): PropertyUse = PropertyUse(this)

    override fun ref(block: Use<PropertyDeclaration>.() -> Unit): PropertyUse = PropertyUse(this)

    override fun generate(output: OutputContext) {
        annotations.forEach {
            output.g(it)
            output.wl()
        }
        output.join(modifiers.map {it.value}, " ", "", " ")
        output.w(if (isMutable) "var" else "val").w(" ").q(name)
        if (type != null) {
            output.w(": ")
            type?.generate(output)
        }
        if(getter != null) {
            output.wl()
            getter?.generate(output)
        }
        if(setter != null) {
            output.wl()
            setter?.generate(output)
        }
        if(init != null) {
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

    fun <T:Any> type(type: kotlin.reflect.KClass<T>, block: ClassTypeUse.() -> Unit = {}) {
        this.type = KClassUse(type).apply(block)
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

    fun modifier(vararg mod: PropertyModifier) = modifiers.addAll(mod.toList())
    fun isLateInit() = modifier(PropertyModifier.LATEINIT)

}

