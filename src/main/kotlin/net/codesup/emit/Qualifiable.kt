package net.codesup.emit

import net.codesup.emit.OutputContext
import net.codesup.emit.QualifiedName

interface Qualifiable {
    val qualifiedName: QualifiedName

    fun generateNameWithDeclaringClass(output: OutputContext) {

    }
}
