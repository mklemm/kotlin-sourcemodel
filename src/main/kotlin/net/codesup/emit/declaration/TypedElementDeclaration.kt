package net.codesup.emit.declaration

import net.codesup.emit.SourceBuilder

/**
 * @author Mirko Klemm 2021-03-19
 *
 */
abstract class TypedElementDeclaration(sourceBuilder: SourceBuilder, name:String): NamedDeclaration(
    sourceBuilder,
    name
)
