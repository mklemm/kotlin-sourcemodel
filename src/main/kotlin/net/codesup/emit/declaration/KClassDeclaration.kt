package net.codesup.emit.declaration

import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2025-09-11
 *
 */
class KClassDeclaration<T : Any>(sourceBuilder: SourceBuilder, val kClass: KClass<T>) : ExternalTypeDeclaration(
    sourceBuilder,
    QualifiedName(kClass.qualifiedName!!)
) {

}
