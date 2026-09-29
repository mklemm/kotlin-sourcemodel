package net.codesup.util.emit.declaration

import net.codesup.util.emit.ExternalSymbol
import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.className

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

    override fun generate(scope: DeclarationOwner, output: OutputContext) {

    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {

    }
}
