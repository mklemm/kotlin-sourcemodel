package net.codesup.util.emit.declaration

/**
 * @author Mirko Klemm 2021-12-22
 *
 */
enum class PropertyModifier(val value: String) {
    PUBLIC("public"),
    OPEN("open"),
    PRIVATE("private"),
    INTERNAL("internal"),
    PROTECTED("protected"),
    LATEINIT("lateinit"),
    OVERRIDE("override"),
    CONST("const")
}
