package net.codesup.util.emit.use

/**
 * @author Mirko Klemm 2021-12-22
 *
 */
enum class AnnotationUseSiteTarget(val value:String) {
    FIELD("field"),
    GET("get"),
    SET("set"),
    FILE("file"),
    PARAM("param"),
    RECEIVER("receiver")
}
