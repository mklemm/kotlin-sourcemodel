package net.codesup.util.emit.declaration

import net.codesup.util.emit.OutputContext
import net.codesup.util.emit.QualifiedName
import net.codesup.util.emit.use.TypeUse
import net.codesup.util.emit.use.Use

class PrimaryConstructorDeclaration(): CallableDeclaration<PrimaryConstructorDeclaration>() {
    val parameters = mutableListOf<PrimaryConstructorParameterDeclaration<*>>()
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(output: OutputContext) {
        if(modifiers.isNotEmpty()) {
            modifiers.forEach {
                output.w(it).w(" ")
            }
            output.w("constructor ")
        }
        output.list(parameters, ", ", "(", ")")
    }

    fun generateBlock(output: OutputContext) {
        block?.let { b ->
            output.wl("init ")
            output.g(b)
        }
    }

    override fun use(block: Use<PrimaryConstructorDeclaration>.() -> Unit): Use<PrimaryConstructorDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<PrimaryConstructorDeclaration>.() -> Unit): Use<PrimaryConstructorDeclaration> {
        TODO("Not yet implemented")
    }

    override fun reportUsedSymbols(c: MutableCollection<QualifiedName>) {
            super.reportUsedSymbols(c)
            parameters.reportUsedSymbols(c)
        }

    fun param(name:String, block: ParameterDeclaration.() -> Unit) = parameters.add(ParameterDeclaration(name).apply(block))
    fun property(name:String, block: PropertyDeclaration.() -> Unit) = parameters.add(PropertyDeclaration(name).apply(block))
}
