package net.codesup.emit.declaration

import net.codesup.emit.SourceBuilder
import net.codesup.emit.className
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2025-09-11
 *
 */
class KClassDeclaration<T : Any>(sourceBuilder: SourceBuilder, val kClass: KClass<T>) : ExternalTypeDeclaration(
    sourceBuilder,
    className(kClass.qualifiedName!!)
)
