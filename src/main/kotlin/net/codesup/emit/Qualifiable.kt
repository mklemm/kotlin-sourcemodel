package net.codesup.emit

interface Qualifiable {
    val qualifiedName: QualifiedName

    fun generateNameWithDeclaringClass(output: OutputContext) {

    }
}
