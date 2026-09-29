package net.codesup.util.emit.use

import net.codesup.util.emit.SourceBuilder
import net.codesup.util.emit.declaration.KClassDeclaration

open class KClassUse<T:Any>(sourceBuilder: SourceBuilder, declaration: KClassDeclaration<T>) : ExternalTypeUse(sourceBuilder, declaration) {

}
