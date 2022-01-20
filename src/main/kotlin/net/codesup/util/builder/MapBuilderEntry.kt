package net.codesup.util.builder

class MapBuilderEntry {
    val key = StringBuilder()
    val values = ListBuilder()

    fun build(): Pair<String, List<String>> = Pair(key.toString(), values.build())

    fun isNotEmpty(): Boolean = key.isNotEmpty()
}
