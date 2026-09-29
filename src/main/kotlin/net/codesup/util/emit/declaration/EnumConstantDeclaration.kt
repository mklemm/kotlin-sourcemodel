package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.Symbol
import net.codesup.util.emit.expressions.ExpressionContext
import net.codesup.util.emit.expressions.ExpressionFactory
import net.codesup.util.emit.use.AnnotationUse

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
class EnumConstantDeclaration(sourceBuilder: SourceBuilder, name: String, val expressionFactory: ExpressionFactory = ExpressionFactory(sourceBuilder)):NamedDeclaration(sourceBuilder, name), ExpressionContext by expressionFactory {
    override val doc: KDocBuilder = KDocBuilder()
    override val annotations: MutableList<AnnotationUse> = mutableListOf()

    override fun generate(
        scope: DeclarationOwner,
        output: OutputContext
    ) {
        output.w(name)
        if(expressionFactory.expression != null) {
            output.w("(")
            output.g(scope,expressionFactory.expression)
            output.w(")")
        }
        output.w(",")
    }

    override fun reportUsedSymbols(c: MutableCollection<Symbol>) {
        expressionFactory.reportUsedSymbols(c)
    }

}
