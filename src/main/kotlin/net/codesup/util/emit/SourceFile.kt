package net.codesup.util.emit

import net.codesup.util.emit.declaration.*
import net.codesup.util.emit.use.AnnotationUse
import net.codesup.util.emit.use.SymbolUser

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

    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.file(packageName, name) {
            if (!packageName.isRoot) {
                output.w("package ")
                packageName.qualifiedName.generate(scope, output)
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
        val myPackage = packageName

        val importedSymbols = usedSymbols
            .filter { u -> sourceBuilder.qualifiedNameOf(u)?.packageParts?.isNotEmpty() == true }

        importedSymbols.mapNotNull{sourceBuilder.qualifiedNameOf(it)}.associateByTo(output.importedSymbolReverseMap, { it }, { LocalName(it.localPart) } )
        declaredSymbols.mapNotNull{sourceBuilder.qualifiedNameOf(it)}.associateByTo(output.importedSymbolReverseMap, { it }, { LocalName(it.localPart) } )

        // Resolve clashes
        val clashes = (usedSymbols + declaredSymbols).mapNotNull{ sourceBuilder.qualifiedNameOf(it) }.distinct().groupBy { it.localPart }.filter { it.value.size > 1 }
        val clashSet = clashes.values.flatten().toSet()
        clashSet.forEach { output.importedSymbolReverseMap.remove(it) }

        val imports = importedSymbols
            .mapNotNull { sourceBuilder.qualifiedNameOf(it) }
            .filter { !clashSet.contains(it) }
            .filter { !(it.classParts.isEmpty() && it.isInSamePackage(myPackage.qualifiedName)) }
            // Keep short-name mappings above, but omit imports supplied by Kotlin/JVM.
            .filter { it.quotedQualifier !in defaultPackageNames }
            .groupBy { it.quotedQualifier }
            .flatMap { (q, vals) -> if (vals.size < wildcardImportLimit) vals.map { it.quotedStringValue } else listOf("$q.*") }

        generateImportBlock(output, generateImportBlock(output, generateImportBlock(output, imports,"kotlin", "kotlinx"),"java", "javax"))
    }

    private fun generateImportBlock(output: OutputContext, imports: List<String>, vararg prefix: String):List<String> {
        val filtered = imports.filter { prefix.isEmpty() || prefix.any { pfx -> it.startsWith("$pfx.") } }
        if(filtered.isNotEmpty()) {
            output.wl()
            filtered.sorted().forEach { output.wl("import $it") }
        }
        return imports.filter { prefix.none { pfx -> it.startsWith("$pfx.") } }
    }


    // Default imports for generated Kotlin/JVM source files.
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
        "kotlin.jvm"
    )

    override val annotations = mutableListOf<AnnotationUse>()

    override val doc: KDocBuilder = KDocBuilder()
    override fun <D:Declaration> addDeclaration(declaration: D):D = declaration.also { declarations.add(it) }

    override fun pathTo(symbol: Symbol): Sequence<Symbol>? =
        if(symbol == this)
            sequenceOf(this)
        else
            declarations.mapNotNull { it as? SymbolOwner }.firstNotNullOfOrNull {
                declaration -> declaration.pathTo(symbol)
            }

}
