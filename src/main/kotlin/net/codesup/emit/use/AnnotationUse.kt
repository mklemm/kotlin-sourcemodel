package net.codesup.util.emit.use

import net.codesup.util.emit.*

class AnnotationUse(val classTypeUse: ClassTypeUse, val isParameter: Boolean = false) : TypeUse(), Qualifiable, ExpressionContext {
    constructor(a: Annotation, isParameter: Boolean = false):this(KClassUse(a.annotationClass), isParameter) {

    }
    var target: AnnotationUseSiteTarget? = null

    override fun generate(output: OutputContext) {
        if(!isParameter) {
            output.w("@${if (target != null) "${target?.value ?: ""}:" else ""}")
        }
        top?.generate(output) ?: output.w("()")
    }

    fun args(block: ConstructorInv.() -> Unit = {}) = ConstructorInv(this, classTypeUse).apply(block).also { top = it }

    override var top: Expression? = null

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(top)
    override val qualifiedName: QualifiedName
        get() = classTypeUse.qualifiedName

}
