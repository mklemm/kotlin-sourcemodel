package net.codesup.emit

import net.codesup.emit.declaration.DeclarationOwner
import kotlin.reflect.KClass

/**
 * @author Mirko Klemm 2021-12-21
 *
 */
abstract class QualifiedName(
    val packageParts: List<String>,
    val classParts: List<String>,
    val localPart: String,
    val nameKind: NameKind
) : Generable {
    init {
        assert(!localPart.contains('.'))
    }

    abstract val qualifier: QualifiedName?
    val parts = packageParts + classParts + localPart
    val quotedQualifier = parts.dropLast(1).joinToString(".") { quote(it) }
    val stringValue = parts.joinToString(".")
    val quotedStringValue = parts.joinToString(".") { quote(it) }
    val isLocal = packageParts.isEmpty()
    val isEmpty = packageParts.isEmpty() && classParts.isEmpty() && localPart.isBlank()

    abstract fun resolve(other: PackageName): PackageName?
    abstract fun resolve(other: ClassName): ClassName?
    abstract fun resolve(other: FunctionName): FunctionName?
    fun resolve(other: String): PackageName = PackageName(packageParts + localPart, other)
    fun isInSamePackage(other: QualifiedName) = packageParts == other.packageParts
    fun isInSameClass(other: QualifiedName) = packageParts == other.packageParts && classParts == other.classParts
    override fun generate(scope: DeclarationOwner, output: OutputContext) {
        output.w(quotedStringValue)
    }
    override fun toString() = stringValue
    override fun hashCode() = stringValue.hashCode()
    override fun equals(other: Any?) = stringValue == other.toString()
}

class PackageName(packageParts: List<String>, localPart: String) :
    QualifiedName(packageParts, emptyList(), localPart, NameKind.Package) {
    override val qualifier: QualifiedName? =
        if (packageParts.isEmpty()) null else PackageName(packageParts.dropLast(1), packageParts.last())

    override fun resolve(other: PackageName): PackageName = PackageName(packageParts + localPart, other.localPart)
    override fun resolve(other: ClassName) = ClassName(packageParts + localPart, other.classParts, other.localPart)
    override fun resolve(other: FunctionName) =
        FunctionName(packageParts + localPart, other.classParts, other.localPart)
}

class ClassName(packageParts: List<String>, classParts: List<String>, localPart: String) :
    QualifiedName(packageParts, emptyList(), localPart, NameKind.Class) {
    override val qualifier: QualifiedName? =
        if (packageParts.isEmpty()) null else PackageName(packageParts.dropLast(1), packageParts.last())

    override fun resolve(other: PackageName): PackageName = PackageName(packageParts + localPart, other.localPart)
    override fun resolve(other: ClassName) = ClassName(packageParts + localPart, classParts, other.localPart)
    override fun resolve(other: FunctionName) =
        FunctionName(packageParts + localPart, other.classParts, other.localPart)
}

class FunctionName(packageParts: List<String>, classParts: List<String>, localPart: String) :
    QualifiedName(packageParts, emptyList(), localPart, NameKind.Function) {
    override val qualifier: QualifiedName? =
        if (packageParts.isEmpty()) null else PackageName(packageParts.dropLast(1), packageParts.last())

    override fun resolve(other: PackageName): PackageName = PackageName(packageParts + localPart, other.localPart)
    override fun resolve(other: ClassName) = ClassName(packageParts + localPart, classParts, other.localPart)
    override fun resolve(other: FunctionName) =
        FunctionName(packageParts + localPart, other.classParts, other.localPart)
}

class LocalName(localPart: String) : QualifiedName(emptyList(), emptyList(), localPart, NameKind.Local) {
    override val qualifier: QualifiedName? = null
    override fun resolve(other: PackageName) = null
    override fun resolve(other: ClassName) = null
    override fun resolve(other: FunctionName) = null
}

fun packageName(vararg s: String) = s.flatMap { it.split('.', '/') }.let { PackageName(it.dropLast(1), it.last()) }
fun packageName(parts: List<String>) = parts.flatMap { it.split('.', '/') }.let { PackageName(it.dropLast(1), it.last()) }
fun className(packageNames: String, parentClassNames: String, localPart: String) =
    ClassName(packageNames.split('.', '/'), parentClassNames.split('$', '.'), localPart)
fun className(packageNames: String, localPart: String) =
    ClassName(packageNames.split('.', '/'), emptyList(), localPart)
fun className(packageName: PackageName, localPart: String) =
    ClassName(packageName.packageParts, emptyList(), localPart)
fun className(parentName: ClassName, localPart: String) =
    ClassName(parentName.packageParts, parentName.classParts + parentName.localPart, localPart)
fun className(qualifiedName: String) = qualifiedName.split('.', '/', '$').let {
    val packageParts = it.takeWhile { it.first().isLowerCase() || it.first().isDigit() }
    val classParts =
        it.dropWhile { it.first().isLowerCase() || it.first().isDigit() }.takeWhile { it.first().isUpperCase() }
    val localPart = classParts.last()
    ClassName(
        packageParts,
        classParts.dropLast(1),
        localPart
    )
}
fun className(kClass: KClass<*>) = className(kClass.qualifiedName!!)

fun functionName(packageNames: String?, parentClassNames: String?, functionName: String) = FunctionName(
    packageNames?.split('.') ?: emptyList(),
    parentClassNames?.split('$', '.') ?: emptyList(),
    functionName
)

fun functionName(packageNames: String?, functionName: String) =
    FunctionName(packageNames?.split('.', '/') ?: emptyList(), emptyList(), functionName)

fun functionName(qualifiedName: String) =
    if (qualifiedName.contains(Regex("[.$/]"))) qualifiedName.split('.', '/', '$').let {
        val packageParts = it.takeWhile { it.first().isLowerCase() || it.first().isDigit() }
        val classParts =
            it.dropWhile { it.first().isLowerCase() || it.first().isDigit() }.takeWhile { it.first().isUpperCase() }
        val localPart =
            it.dropWhile { it.first().isLowerCase() || it.first().isDigit() }.dropWhile { it.first().isUpperCase() }
                .takeWhile { it.first().isLowerCase() || it.first().isDigit() }.lastOrNull()
        FunctionName(
            if (classParts.isEmpty()) packageParts.dropLast(1) else packageParts,
            classParts,
            if (localPart == null || classParts.isEmpty()) packageParts.last() else localPart
        )
    }
    else
        FunctionName(emptyList(), emptyList(), qualifiedName)

fun localName(name: String):LocalName = LocalName(name)
fun emptyName():LocalName = LocalName("")

enum class NameKind {
    Package,
    Class,
    Function,
    Local
}
