package net.codesup.emit

import net.codesup.emit.declaration.DeclarationScope

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface Generable {
    fun generate(scope: DeclarationScope, output: OutputContext)
}
