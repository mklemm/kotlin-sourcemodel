package net.codesup.emit

import net.codesup.emit.declaration.DeclarationOwner

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface Generable {
    fun generate(scope: DeclarationOwner, output: OutputContext)
}
