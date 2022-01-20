package net.codesup.util.builder

class MapBuilder {
    val pairList = mutableListOf<Pair<String, List<String>>>()
    var current: MapBuilderEntry = MapBuilderEntry()

    fun next(): MapBuilderEntry {
        pairList.add(current.build())
        current = MapBuilderEntry()
        return current
    }

    fun build(): Map<String, List<String>> {
        if (current.isNotEmpty()) {
            next()
        }
        return pairList.groupBy({ it.first }, { it.second }).map { Pair(it.key, it.value.flatten()) }.toMap()
    }
}
