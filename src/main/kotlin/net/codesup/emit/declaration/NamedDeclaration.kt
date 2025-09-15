package net.codesup.emit.declaration

import net.codesup.emit.SourceBuilder

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
abstract class NamedDeclaration(override val sourceBuilder: SourceBuilder, override val name: String): Declaration {
   override var metadata: Any? = null
}
