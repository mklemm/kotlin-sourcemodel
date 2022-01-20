package net.codesup.emit.declaration

import net.codesup.emit.Generable
import net.codesup.emit.OutputContext

/**
 * @author Mirko Klemm 2022-01-12
 *
 */
class KDocBuilder: Generable {
    val lines = mutableListOf<String>()
    override fun generate(output: OutputContext) {
        if(lines.isNotEmpty()) {
            output.wl("/**")
            lines.forEach {
                output.w(" * ")
                output.wl(it)
            }
            output.wl(" */")
        }
    }
    fun line(s:String) = lines.add(s)
}
