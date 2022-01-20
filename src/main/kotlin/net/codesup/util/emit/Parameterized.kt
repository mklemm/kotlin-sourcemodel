package net.codesup.util.emit

import net.codesup.util.emit.declaration.ParameterDeclaration

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
interface Parameterized {
    val parameters:MutableList<ParameterDeclaration>
    fun param(name:String, block: ParameterDeclaration.() -> Unit) = ParameterDeclaration(name).apply(block).also { parameters.add(it) }
}
