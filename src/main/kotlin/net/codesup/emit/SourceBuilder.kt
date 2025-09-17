package net.codesup.emit

import net.codesup.emit.declaration.*
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.ExternalTypeUse
import net.codesup.emit.use.KClassUse
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.jvm.javaMethod

/**
 * @author Mirko Klemm 2021-03-18
 *
 */
class SourceBuilder : Generable, SymbolOwner {
    val packages = mutableMapOf<QualifiedName, PackageDeclaration>()

    override fun pathTo(symbol: Symbol): Sequence<Symbol>? = packages.values.firstNotNullOfOrNull {
        it.pathTo(symbol)
    }

    private val externalTypeDeclarations = mutableMapOf<QualifiedName, ExternalTypeDeclaration>()
    private val externalFunctionDeclarations = mutableMapOf<QualifiedName, ExternalFunctionDeclaration>()
    private val kClassDeclarations = mutableMapOf<KClass<*>, KClassDeclaration<*>>()

    fun externalType(qualifiedName: QualifiedName): ExternalTypeDeclaration =
        externalTypeDeclarations.computeIfAbsent(qualifiedName) {
            ExternalTypeDeclaration(this, qualifiedName)
        }

    fun externalType(qualifiedName: String): ExternalTypeDeclaration = externalType(className(qualifiedName))

    fun externalFunction(qualifiedName: QualifiedName): ExternalFunctionDeclaration =
        externalFunctionDeclarations.computeIfAbsent(qualifiedName) {
            ExternalFunctionDeclaration(this, qualifiedName)
        }

    fun <R> externalFunction(function: KFunction<R>): ExternalFunctionDeclaration =
        externalFunctionDeclarations.computeIfAbsent(function.qualifiedName) {
            ExternalFunctionDeclaration(this, function.qualifiedName)
        }

    fun externalFunction(qualifiedName: String): ExternalFunctionDeclaration =
        externalFunction(functionName(qualifiedName))

    fun <T : Any> externalType(kClass: KClass<T>): KClassDeclaration<T> = kClassDeclarations.computeIfAbsent(kClass) {
        KClassDeclaration(this, kClass)
    } as KClassDeclaration<T>

    fun <T : Any> typeUse(cls: KClass<T>, block: KClassUse<T>.() -> Unit = {}) = KClassUse(
        this, externalType(cls)
    ).apply(block)

    fun typeUse(declaration: ExternalTypeDeclaration, block: ExternalTypeUse.() -> Unit = {}) = ExternalTypeUse(
        this, declaration
    ).apply(block)

    fun typeUse(qName: QualifiedName, block: ExternalTypeUse.() -> Unit = {}) = ExternalTypeUse(
        this, externalType(qName)
    ).apply(block)

    fun typeUse(qName: String, block: ExternalTypeUse.() -> Unit = {}) = ExternalTypeUse(
        this, externalType(qName)
    ).apply(block)

    fun typeUse(declaration: ClassDeclaration, block: ClassTypeUse.() -> Unit = {}) = ClassTypeUse(
        this, declaration
    ).apply(block)

    fun <T : Any> typeUse(typeDeclaration: KClassDeclaration<T>, block: ExternalTypeUse.() -> Unit = {}) =
        KClassUse<T>(this, typeDeclaration).apply(block)

    fun _package(name: QualifiedName, block: PackageDeclaration.() -> Unit) =
        PackageDeclaration(this, name).apply(block).also { packages[name] = it }

    fun _package(name: String, block: PackageDeclaration.() -> Unit) = packageName(name).let { pn ->
        PackageDeclaration(
            this,
            pn
        ).apply(block).also { packages[pn] = it }
    }

    val rootPackage = PackageDeclaration(this, emptyName())

    val unitType = KClassDeclaration(this, Unit::class)
    val anyType = KClassDeclaration(this, Any::class)
    val nothingType = KClassDeclaration(this, Nothing::class)
    val stringType = KClassDeclaration(this, String::class)

    override fun generate(scope: DeclarationScope, output: OutputContext) {
        packages.values.forEach {
            it.generate(it, output)
        }
    }

    fun generate(output: OutputContext) = generate(packages.values.first(), output)

    val KClass<*>.nameWithParentClass: String
        get() = enclosingClass?.let { "${it.nameWithParentClass}.$simpleName" } ?: simpleName!!
    val <T : Any> KClass<T>.enclosingClass: KClass<*>? get() = java.enclosingClass?.kotlin
    val <T : Any> KClass<T>.packageName: String get() = java.packageName
}

fun sourceBuilder(block: SourceBuilder.() -> Unit): SourceBuilder = SourceBuilder().apply(block)

val <R> KFunction<R>.qualifiedName: QualifiedName
    get() = functionName(
        javaMethod?.declaringClass?.packageName,
        if (instanceParameter == null) null else javaMethod?.declaringClass?.name?.substringAfterLast('.'),
        name
    )
