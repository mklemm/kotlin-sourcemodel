package net.codesup.emit.declaration

/**
 * @author Mirko Klemm 2022-01-21
 *
 */
enum class ClassifierKind(val keyword:String) {
    CLASS("class"),
    INTERFACE("interface"),
    ENUM("enum class"),
    ANNOTATION("annotation class"),
    DATACLASS("data class")
}
