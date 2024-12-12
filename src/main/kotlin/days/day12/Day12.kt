package days.day12

import days.Day
import days.util.get4Neighbours
import days.util.parseToMap

class Day12: Day(false) {
    override fun partOne(): Any {
        val map = readInput().parseToMap()
        return map.values.toSet().sumOf { value ->

            println("value: $value")

            return@sumOf getFields(map, value).sumOf{field -> field.sumOf { key ->
                    key.get4Neighbours().filter { map[it] != value }.size
                } * field.size
            }
        }
    }

    fun getFields(map: HashMap<Pair<Int, Int>, Char>, value: Char): Set<Set<Pair<Int, Int>>> {
        val fields = mutableSetOf<Set<Pair<Int, Int>>>()
        val visited = mutableSetOf<Pair<Int, Int>>()

        map.filter { it.value == value }.forEach { (key, _) ->
            if (key !in visited) {
                val field = mutableSetOf<Pair<Int, Int>>()
                val queue = mutableListOf(key)
                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    if (current !in visited) {
                        visited.add(current)
                        field.add(current)
                        queue.addAll(current.get4Neighbours().filter { map[it] == value })
                    }
                }
                fields.add(field)
            }
        }

        return fields
    }

    override fun partTwo(): Any {
        return "day 12 part 2 not Implemented"
    }
}

