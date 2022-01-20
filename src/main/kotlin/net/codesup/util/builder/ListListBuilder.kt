package net.codesup.util.builder

class ListListBuilder {
    val values = mutableListOf<List<String>>()
    var current = ListBuilder()
    fun next(): ListBuilder {
        values.add(current.build())
        current = ListBuilder()
        return current
    }
    fun build():List<List<String>> {
        if(current.isNotEmpty()) {
            next()
        }
        return values
    }
}
