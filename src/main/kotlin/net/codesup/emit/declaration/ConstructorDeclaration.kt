package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.Parameterized
import net.codesup.emit.QualifiedName
import net.codesup.emit.use.Use

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
