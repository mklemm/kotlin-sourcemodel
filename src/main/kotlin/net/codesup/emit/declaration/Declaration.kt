package net.codesup.emit.declaration

import net.codesup.emit.Annotatable
import net.codesup.emit.Expression
import net.codesup.emit.QualifiedName
import net.codesup.emit.use.Use

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface Declaration<T: Declaration<T>>: Expression, Annotatable {
    val declarations:MutableList<Declaration<*>>
    val name:String
    val doc: KDocBuilder

    fun use(block: Use<T>.() -> Unit = {}): Use<T>
    fun ref(block: Use<T>.() -> Unit): Use<T>

    fun reportDeclaredSymbols(parentName: QualifiedName, c:MutableCollection<QualifiedName>) {
        val myQualifiedName = parentName.resolve(name)
        c.add(myQualifiedName)
        declarations.forEach { it.reportDeclaredSymbols(myQualifiedName, c) }
    }

    fun doc(s:String): KDocBuilder = doc.apply { lines.add(s) }
    fun doc(block: KDocBuilder.() -> Unit) = doc.apply(block)
}
