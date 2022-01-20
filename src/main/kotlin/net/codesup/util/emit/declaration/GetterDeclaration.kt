package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.use.Use

class GetterDeclaration: CallableDeclaration<GetterDeclaration>() {
    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) = c.add(block)
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(output: OutputContext) {
        output.w(modifiers.joinToString(" "))
        output.w("get()")
        block?.generate(output)
    }

    override fun use(block: Use<GetterDeclaration>.() -> Unit): Use<GetterDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<GetterDeclaration>.() -> Unit): Use<GetterDeclaration> {
        TODO("Not yet implemented")
    }
}
