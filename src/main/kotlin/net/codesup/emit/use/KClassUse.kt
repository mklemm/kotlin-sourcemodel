package net.codesup.emit.use

import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.KClassDeclaration

open class KClassUse<T:Any>(sourceBuilder: SourceBuilder, declaration: KClassDeclaration<T>) : ExternalTypeUse(sourceBuilder, declaration) {

}
