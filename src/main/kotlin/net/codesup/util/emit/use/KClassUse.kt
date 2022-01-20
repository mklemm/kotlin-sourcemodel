package net.codesup.util.emit.use

import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.declaration.Package
import kotlin.reflect.KClass

open class KClassUse<T:Any>(val kClass: KClass<T>) : ClassTypeUse(QualifiedName(kClass.qualifiedName ?: "<unnamed>")) {
    companion object {
        val unit = KClassUse(Unit::class)
        val any = KClassUse(Any::class)
        val nothing = KClassUse(Nothing::class)
        val string = KClassUse(String::class)
    }

    override fun copy(qualifiedName: QualifiedName, block:ClassTypeUse.() -> Unit) = KClassUse(kClass).apply {
        this.isNullable = this@KClassUse.isNullable
        this.annotations.addAll(this@KClassUse.annotations)
        this.typeArguments.addAll(this@KClassUse.typeArguments)
    }
}
