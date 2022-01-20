package net.codesup.emit.declaration

/**
 * @author Mirko Klemm 2021-12-22
 *
 */
enum class ObjectModifier(val value: String) {
    PUBLIC("public"), COMPANION("companion"),
    SEALED("sealed"), PRIVATE("private"),
    INTERNAL("internal"), PROTECTED("protected")
}
