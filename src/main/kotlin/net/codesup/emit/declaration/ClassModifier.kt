package net.codesup.emit.declaration

/**
 * @author Mirko Klemm 2021-12-22
 *
 */
enum class ClassModifier(val value: String) {
    PUBLIC("public"), OPEN("open"),
    SEALED("sealed"), PRIVATE("private"),
    INTERNAL("internal"), PROTECTED("protected"),
    DATA("data"), INLINE("inline"), ANNOTATION("annotation"),
    ACTUAL("actual")
}
