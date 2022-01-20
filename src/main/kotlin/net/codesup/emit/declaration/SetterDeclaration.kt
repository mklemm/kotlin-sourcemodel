package net.codesup.emit.declaration

import net.codesup.emit.OutputContext
import net.codesup.emit.use.Use

class SetterDeclaration(val parameterName:String = "value"): CallableDeclaration<SetterDeclaration>() {
    override val doc: KDocBuilder = KDocBuilder()
    override fun generate(output: OutputContext) {
        output.w(modifiers.joinToString(" "))
        output.w("set(").q(parameterName).w(")")
        block?.generate(output)
    }

    override fun use(block: Use<SetterDeclaration>.() -> Unit): Use<SetterDeclaration> {
        TODO("Not yet implemented")
    }

    override fun ref(block: Use<SetterDeclaration>.() -> Unit): Use<SetterDeclaration> {
        TODO("Not yet implemented")
    }
}
