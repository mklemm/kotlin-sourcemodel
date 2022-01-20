package net.codesup.util.emit

import net.codesup.util.emit.declaration.Declaration
import net.codesup.util.emit.declaration.DeclarationOwner
import net.codesup.util.emit.declaration.NamedDeclaration
import net.codesup.util.emit.declaration.Package
import net.codesup.util.emit.use.AnnotationUse
import net.codesup.util.emit.use.SymbolUser
import net.codesup.util.emit.use.Use

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class SourceFile(val packageName: Package, override val name: String) : Generable, SymbolUser,
    DeclarationOwner<SourceFile>,
    NamedDeclaration<SourceFile> {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(declarations)
    override fun reportDeclaredSymbols(parentName: QualifiedName, c: MutableCollection<QualifiedName>) {
        declarations.forEach { it.reportDeclaredSymbols(parentName, c) }
    }

    override val declarations = mutableListOf<Declaration<*>>()

    override fun generate(output: OutputContext) {
        output.file(packageName.toPath().resolve("${name}.kt")) {
            if (!packageName.isRoot) {
                output.w("package ")
                packageName.qualifiedName.generate(output)
                output.wl()
                output.wl()
            }
            generateImports(output)
            output.wl()

            declarations.forEach { it.generate(output) }
        }
    }

    fun generateImports(output: OutputContext) {
        val wildcardImportLimit = 8

        val usedSymbols = mutableSetOf<QualifiedName>().also { reportUsedSymbols(it) }
        val declaredSymbols = mutableSetOf<QualifiedName>().also { reportDeclaredSymbols(packageName.qualifiedName, it) }

        val importedSymbols = usedSymbols
            .filter { u -> declaredSymbols.none { l -> l == u } }
            .filter { u -> usedSymbols.none { d -> d.qualifier != u.qualifier && d.localPart == u.localPart } }
            .filter { u -> declaredSymbols.none { d -> d.qualifier != u.qualifier && d.localPart == u.localPart } }

        val imports = importedSymbols
            .filter { it.qualifier != packageName.qualifiedName }
            .groupBy { it.quotedQualifier }
            .flatMap { (q, vals) -> if (vals.size < wildcardImportLimit) vals.map { it.quotedStringValue } else listOf("$q.*") }

        importedSymbols.associateByTo(output.importedSymbolReverseMap, { it }, { QualifiedName(it.localPart) })
        declaredSymbols.associateByTo(output.importedSymbolReverseMap, { it }, { QualifiedName(it.localPart) })

        // Resolve clashes
        val clashes = (usedSymbols + declaredSymbols).groupBy { it.localPart }.filter { it.value.size > 1 }
        clashes.values.flatten().forEach { output.importedSymbolReverseMap.remove(it) }

        imports.sortedBy { p ->
            when (p) {
                "kotlin" -> "0-$p"
                "java" -> "1-$p"
                "javax" -> "2-$p"
                else -> "3-$p"
            }
        }.forEach {
            output.wl("import $it")
        }
    }


    private val defaultPackageNames = setOf(
        "kotlin",
        "kotlin.annotation",
        "kotlin.collections",
        "kotlin.comparisons",
        "kotlin.io",
        "kotlin.ranges",
        "kotlin.sequences",
        "kotlin.text",
        "java.lang",
        "kotlin.jvm",
        "kotlin.js"
    )

    override val annotations = mutableListOf<AnnotationUse>()

    override fun use(block: Use<SourceFile>.() -> Unit): Use<SourceFile> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<SourceFile>.() -> Unit): Use<SourceFile> {
        TODO("Not yet implemented")
    }
}
