package net.codesup.util.emit.declaration

import net.codesup.util.emit.*
import net.codesup.util.emit.use.AnnotationUse
import net.codesup.util.emit.use.Use
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class Package(val qualifiedName: QualifiedName): Generable, Expression, DeclarationOwner<Package> {
    override val name = qualifiedName.toString()
    override val doc: KDocBuilder = KDocBuilder()
    override val annotations = mutableListOf<AnnotationUse>()
    override val declarations: MutableList<Declaration<*>> = mutableListOf()
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(declarations)

    override fun generate(output: OutputContext) {
        declarations.forEach { it.generate(output) }
    }

    val isRoot = qualifiedName.isEmpty()

    fun toPath(): Path = Paths.get(toString().replace('.','/'))

    override fun toString(): String = qualifiedName.toString()
    override fun equals(other: Any?): Boolean = qualifiedName == other
    override fun hashCode(): Int = qualifiedName.hashCode()

    fun _file(name:String, block: SourceFile.() -> Unit) = sourceFile(this, name, block)

    override fun use(block: Use<Package>.() -> Unit): Use<Package> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<Package>.() -> Unit): Use<Package> {
        TODO("Not yet implemented")
    }

    fun _package(name: String):Package = Package(qualifiedName.resolve(name))
}
