package net.codesup.util.builder

class ListBuilder {
    val values = mutableListOf<StringBuilder>()
    var current = StringBuilder()
    fun next():StringBuilder {
        values.add(current)
        current = StringBuilder()
        return current
    }
    fun build():List<String> {
        if(current.isNotEmpty()) {
            next()
        }
        return values.map { it.toString() }
    }
    fun isNotEmpty():Boolean = values.isNotEmpty()
}
