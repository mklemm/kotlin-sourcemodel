package net.codesup.emit

import net.codesup.emit.OutputContext

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
interface Generable {
    fun generate(output: OutputContext)
}
