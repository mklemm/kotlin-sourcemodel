package net.codesup.emit

import net.codesup.emit.declaration.*
import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.SymbolUser

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class SourceFile(sourceBuilder: SourceBuilder, val packageName: PackageDeclaration, name: String) : NamedDeclaration(sourceBuilder, name), Generable, SymbolUser,
    DeclarationScope {
    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        declarations.reportUsedSymbols(c)
    }
    override fun reportDeclaredSymbols(c: MutableCollection<Symbol>) {
        declarations.forEach {
            it.reportDeclaredSymbols( c)
        }
    }

    override val declarations = mutableListOf<Declaration>()

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        output.file(packageName.toPath().resolve("${name}.kt")) {
            if (!packageName.isRoot) {
                output.w("package ")
                packageName.qualifiedName.generate(scope, output)
                output.wl()
                output.wl()
            }
            generateImports(output)
            declarations.forEach {
                output.wl()
                it.generate(scope, output)
            }
        }
    }

    fun generateImports(output: OutputContext) {
        val wildcardImportLimit = 8

        val usedSymbols = mutableSetOf<Symbol>().also { reportUsedSymbols(it) }
        val declaredSymbols = mutableSetOf<Symbol>().also { reportDeclaredSymbols(it) }
        val myPackage = sourceBuilder.pathTo(this)?.last()

        val importedSymbols = usedSymbols
            .filter { u -> myPackage?.qualifiedNameOf(u) == null }

        val imports = importedSymbols
            .mapNotNull { sourceBuilder.qualifiedNameOf(it) }
            .groupBy { it.quotedQualifier }
            .flatMap { (q, vals) -> if (vals.size < wildcardImportLimit) vals.map { it.quotedStringValue } else listOf("$q.*") }

        importedSymbols.mapNotNull{sourceBuilder.qualifiedNameOf(it)}.associateByTo(output.importedSymbolReverseMap, { it }, { QualifiedName(it.localPart) })
        declaredSymbols.mapNotNull{sourceBuilder.qualifiedNameOf(it)}.associateByTo(output.importedSymbolReverseMap, { it }, { QualifiedName(it.localPart) })

        // Resolve clashes
        val clashes = (usedSymbols + declaredSymbols).mapNotNull{sourceBuilder.qualifiedNameOf(it)}.groupBy { it.localPart }.filter { it.value.size > 1 }
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

    override val doc: KDocBuilder = KDocBuilder()
    override fun addDeclaration(declaration: Declaration) {
        declarations.add(declaration)
    }
}
