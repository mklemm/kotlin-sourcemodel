package net.codesup.emit.declaration

import net.codesup.emit.ExternalSymbol
import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName
import net.codesup.emit.SourceBuilder
import net.codesup.emit.Symbol
import net.codesup.emit.className
import net.codesup.emit.use.AnnotationUse

/**
 * @author Mirko Klemm 2025-09-11
 *
 */
open class ExternalTypeDeclaration(sourceBuilder: SourceBuilder, override val qualifiedName: QualifiedName):TypeDeclaration(
    sourceBuilder,
    qualifiedName.localPart
), ExternalSymbol {
    constructor(sourceBuilder: SourceBuilder, name: String) : this(sourceBuilder, className(name))

    override val doc: KDocBuilder = KDocBuilder()

    override fun generate(scope: DeclarationScope, output: OutputContext) {

    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {

    }
}
