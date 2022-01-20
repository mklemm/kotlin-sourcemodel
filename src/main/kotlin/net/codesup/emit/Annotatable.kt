package net.codesup.emit

import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
interface Annotatable {
    val annotations:MutableList<AnnotationUse>
    fun annotate(annotationUse: AnnotationUse): AnnotationUse {
        annotations.add(annotationUse)
        return annotationUse
    }
    fun annotate(remainingName: QualifiedName, block: AnnotationUse.() -> Unit): AnnotationUse = AnnotationUse(
        ClassTypeUse(remainingName)
    ).apply(block).apply{ annotations.add(this) }
    fun annotate(name: String, block: AnnotationUse.() -> Unit): AnnotationUse = AnnotationUse(ClassTypeUse(name)).apply(block).apply{ annotations.add(this) }
    fun annotate(annotationClass: ClassTypeUse, block: AnnotationUse.() -> Unit) = AnnotationUse(annotationClass).apply(block).apply { annotations.add(this) }
    fun <T:Annotation> annotate(annotationClass: kotlin.reflect.KClass<T>, block: AnnotationUse.() -> Unit = {}) = AnnotationUse(
        KClassUse<T>(annotationClass)
    ).apply(block).apply { annotations.add(this) }
    fun <A:Annotation> annotate(annotation:A, block: AnnotationUse.() -> Unit = {}) = AnnotationUse(annotation).apply(block).apply { annotations.add(this) }
}

