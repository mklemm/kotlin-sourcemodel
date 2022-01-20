package net.codesup.util.emit

interface Qualifiable {
    val qualifiedName: QualifiedName

    fun generateNameWithDeclaringClass(output: OutputContext) {

    }
}
