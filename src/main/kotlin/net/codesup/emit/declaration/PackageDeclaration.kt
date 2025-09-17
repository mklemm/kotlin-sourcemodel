package net.codesup.emit.declaration

import net.codesup.emit.*
import net.codesup.emit.use.AnnotationUse
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
class PackageDeclaration(sourceBuilder: SourceBuilder, override val qualifiedName: QualifiedName): NamedDeclaration(
    sourceBuilder,
    qualifiedName.localPart
), DeclarationOwner, Generable {
    override val name = qualifiedName.toString()
    override val doc: KDocBuilder = KDocBuilder()
    override val annotations = mutableListOf<AnnotationUse>()
    override val declarations: MutableList<Declaration> = mutableListOf()
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) = c.add(declarations)

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        declarations.forEach { it.generate(scope, output) }
    }

    val isRoot = qualifiedName.isEmpty

    fun toPath(): Path = Paths.get(toString().replace('.','/'))

    override fun toString(): String = qualifiedName.toString()
    override fun equals(other: Any?): Boolean = qualifiedName == other
    override fun hashCode(): Int = qualifiedName.hashCode()

    fun _file(name:String, block: SourceFile.() -> Unit) = sourceFile(this, name, block)

    /**
     * Generates a class file with a single class declaration
     * @param name  Name of the new class, will set the filename to the same string
     * @param block Definition block for the new class declaration
     */
    fun _class(name:String, block: ClassDeclaration.() -> Unit):SourceFile = sourceFile(this, name) {
        _class(name, block)
    }

    fun _package(name: String): PackageDeclaration = addPackage(PackageDeclaration(sourceBuilder, qualifiedName.resolve(name)))

    private fun addFile(file: SourceFile): SourceFile =
        file.also { declarations.add(file) }
    private fun addPackage(pkg: PackageDeclaration): PackageDeclaration =
        pkg.also { declarations.add(pkg) }


    fun sourceFile(packageName: PackageDeclaration, name: String, block: SourceFile.() -> Unit): SourceFile =
        addFile(SourceFile(sourceBuilder, packageName, name).apply(block))

}
