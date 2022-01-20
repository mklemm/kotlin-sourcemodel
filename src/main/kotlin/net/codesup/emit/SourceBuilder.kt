package net.codesup.emit

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.declaration.Package
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse
import net.codesup.emit.Generable
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class SourceBuilder: Generable {
    val packages = mutableMapOf<QualifiedName, Package>()

    fun <T:Any> typeUse(cls: KClass<T>, block: ClassTypeUse.() -> Unit = {}) = KClassUse(cls).apply(block)
    fun _package(name: QualifiedName, block: Package.() -> Unit) = Package(name).apply(block).also { packages[name] = it }
    fun _package(name:String, block: Package.() -> Unit) = QualifiedName(name).let { pn -> Package(pn).apply(block).also { packages[pn] = it } }

    override fun generate(output: OutputContext) {
        packages.values.forEach {
            it.generate(output)
        }
    }

    val KClass<*>.nameWithParentClass:String get() = enclosingClass?.let { "${it.nameWithParentClass}.$simpleName" } ?: simpleName!!
    val <T:Any> KClass<T>.enclosingClass:KClass<*>? get() = java.enclosingClass?.kotlin
    val <T:Any> KClass<T>.packageName:String get() = java.packageName
}

fun sourceBuilder(block: SourceBuilder.()->Unit): SourceBuilder = SourceBuilder().apply(block)
