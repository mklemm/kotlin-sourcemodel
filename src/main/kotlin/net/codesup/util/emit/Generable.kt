package net.codesup.util.emit

import net.codesup.util.emit.declaration.DeclarationOwner

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface Generable {
    fun generate(scope: DeclarationOwner, output: OutputContext)
}
