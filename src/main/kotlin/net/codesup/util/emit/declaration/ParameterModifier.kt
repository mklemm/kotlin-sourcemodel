package net.codesup.util.emit.declaration

/**
 * @author Mirko Klemm 2021-12-28
 *
 */
enum class ParameterModifier(val value:String) {
    VARARG("vararg"),
    CROSSINLINE("crossinline"),
    NOINLINE("noinline")
}
