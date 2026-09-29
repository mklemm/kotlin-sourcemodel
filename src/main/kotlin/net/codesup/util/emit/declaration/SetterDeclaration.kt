package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.SourceBuilder

class SetterDeclaration(sourceBuilder: SourceBuilder, val parameterName:String = "value"): CallableDeclaration(sourceBuilder) {
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(modifiers.joinToString(" "))
        output.w("set(").q(parameterName).w(")")
        block?.generate(scope, output)
    }
}
