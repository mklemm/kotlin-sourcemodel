package net.codesup.emit

import net.codesup.emit.declaration.KClassDeclaration
import net.codesup.emit.declaration.TypeDeclaration
import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
interface Annotatable {
    val sourceBuilder: SourceBuilder
    val annotations:MutableList<AnnotationUse>
    fun annotate(annotationUse: AnnotationUse): AnnotationUse {
        annotations.add(annotationUse)
        return annotationUse
    }
    fun annotate(annotationClass: TypeDeclaration, block: AnnotationUse.() -> Unit) = AnnotationUse(sourceBuilder, annotationClass).apply(block).apply { annotations.add(this) }
    fun <T:Annotation> annotate(annotationClass: kotlin.reflect.KClass<T>, block: AnnotationUse.() -> Unit = {}) = AnnotationUse(sourceBuilder, annotationClass).apply(block).apply { annotations.add(this) }
    fun <A:Annotation> annotate(annotation:A, block: AnnotationUse.() -> Unit = {}) = AnnotationUse(
        this.sourceBuilder,
        annotation
    ).apply(block).apply { annotations.add(this) }
}

