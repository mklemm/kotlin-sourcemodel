package net.codesup.emit

/**
 * @author Mirko Klemm 2021-12-21
 *
 */
class QualifiedName(val parts:List<String>): Generable {
    constructor(vararg nameParts:String):this(nameParts.map { it.split('.') }.flatten())
    constructor(parent: QualifiedName?, vararg nameParts:String):this(parent, nameParts.map { it.split('.') }.flatten())
    constructor(parent: QualifiedName?, nameParts:List<String>):this(parent?.parts?.let { it + nameParts } ?: nameParts)
    constructor(parent: QualifiedName?, other: QualifiedName):this(parent?.parts?.let { it + other.parts } ?: other.parts)
    constructor(name:String, other: QualifiedName):this(QualifiedName(name), other)

    val localPart = parts.last()
    val qualifier: QualifiedName? = if(parts.size > 1) QualifiedName(parts.dropLast(1)) else null
    val quotedQualifier = parts.dropLast(1).joinToString(".") { quote(it) }
    val stringValue = parts.joinToString(".")
    val quotedStringValue = parts.joinToString(".") { quote(it) }

    override fun generate(output: OutputContext) {
        output.w(quotedStringValue)
    }

    fun resolve(other: QualifiedName) = QualifiedName(this, other.parts)
    fun resolve(other: String) = QualifiedName(this, other)
    fun relativize(other: QualifiedName) = QualifiedName(other.parts.foldIndexed(mutableListOf()) { i, l, s -> l.apply{if(parts[i] != s) add(s)} })
    fun isEmpty() = parts.isEmpty()
    override fun toString() = stringValue
    override fun hashCode() = stringValue.hashCode()
    override fun equals(other: Any?) = stringValue == other.toString()
}

fun qn(vararg s: String) = QualifiedName(*s)
