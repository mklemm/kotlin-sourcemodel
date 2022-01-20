package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.Parameterized
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.use.Use

class ConstructorDeclaration : CallableDeclaration<ConstructorDeclaration>(), Parameterized {
    override val parameters = mutableListOf<ParameterDeclaration>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(output: OutputContext) {
        doc.generate(output)
        modifiers.forEach {
            output.w(it).w(" ")
        }
        output.w("constructor ")
        output.list(parameters, ", ", "(", ")")
        output.g(block)
    }

    internal fun generateBlock(output: OutputContext) {

    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
        super.reportUsedSymbols(c)
        parameters.reportUsedSymbols(c)
    }

    override fun use(block: Use<ConstructorDeclaration>.() -> Unit): Use<ConstructorDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<ConstructorDeclaration>.() -> Unit): Use<ConstructorDeclaration> {
        TODO("Not yet implemented")
    }

}
