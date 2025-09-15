package net.codesup.emit.declaration

import net.codesup.emit.*
import net.codesup.emit.expressions.Expression
import net.codesup.emit.use.AnnotationUse
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class PackageDeclaration(sourceBuilder: SourceBuilder, val qualifiedName: QualifiedName): NamedDeclaration(
    sourceBuilder,
    qualifiedName.localPart
), Generable, DeclarationScope {
    override val name = qualifiedName.toString()
    override val doc: KDocBuilder = KDocBuilder()
    override val annotations = mutableListOf<AnnotationUse>()
    override val declarations: MutableList<Declaration> = mutableListOf()
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(declarations)

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        declarations.forEach { it.generate(scope, output) }
    }

    val isRoot = qualifiedName.isEmpty()

    fun toPath(): Path = Paths.get(toString().replace('.','/'))

    override fun toString(): String = qualifiedName.toString()
    override fun equals(other: Any?): Boolean = qualifiedName == other
    override fun hashCode(): Int = qualifiedName.hashCode()

    fun _file(name:String, block: SourceFile.() -> Unit) = sourceFile(this, name, block)

    fun _package(name: String): PackageDeclaration = PackageDeclaration(sourceBuilder, qualifiedName.resolve(name))
    override fun addDeclaration(declaration: Declaration) {
        declarations.add(declaration)
    }
}
