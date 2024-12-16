package days.day12

import days.Day
import days.util.*

class Day12: Day(false) {
    override fun partOne(): Any {
        val map = readInput().parseToMap().filter { it.value != '.' }
        return map.values.toSet().sumOf { value ->
            return@sumOf getFields(map, value).sumOf{field -> field.sumOf { key ->
                    key.get4Neighbours().filter { map[it] != value }.size
                } * field.size
            }
        }
    }

    fun getFields(map: Map<Pair<Int, Int>, Char>, value: Char): Set<Set<Pair<Int, Int>>> {
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
        val map = makeItFat(readInput()).parseToMap().filter { it.value != '.' }
        fun Set<Pair<Int, Int>>.walkEdge(): Int {
            val edgeTiles = this.filter { item -> item.get4Neighbours().filter { map[it] == map[item] }.size < 4 }.sortedBy { it.first }

            //Outer circle
            val startDirection = Direction.WEST
            val actualStart = edgeTiles.first().north()
            var current = actualStart
            var direction = startDirection

            var res = 0
            val visited = mutableSetOf<Pair<Int, Int>>()

            do {
                val next = current.move(direction)
                if (next.move(direction.turnLeft()) !in this) {
                    direction = direction.turnLeft()
                    current = next.move(direction)
                    visited.add(current.move(direction.turnLeft()))
                    res++
                } else if (next in this) {
                    direction = direction.turnRight()
                    visited.add(next)
                    res++
                } else {
                    visited.add(next.move(direction.turnLeft()))
                    current = next
                }
            }while (current != actualStart || direction != startDirection)

            while (visited.size < edgeTiles.size) {
                res += hole(visited, edgeTiles, map)
            }

            return res
        }
        return map.values.toSet().sumOf { value ->
            return@sumOf getFields(map, value).sumOf{field -> field.walkEdge() * field.size / 4 }
        }
    }
    private fun hole(visited: MutableSet<Pair<Int, Int>>, edgeTiles: List<Pair<Int, Int>>, map: Map<Pair<Int, Int>, Char>): Int {

        val startDirection = Direction.WEST
        val actualStart = edgeTiles.first{it !in visited}.south()
        var current = actualStart
        var direction = startDirection
        var res = 0
        do {
            //map.visualize(listOf(setOf(current) to ConsoleColors.GREEN_BACKGROUND, visited to ConsoleColors.RED_BACKGROUND, edgeTiles.toSet() to ConsoleColors.BLUE_BACKGROUND, ))
            val next = current.move(direction)
            if (next in edgeTiles) {
                direction = direction.turnLeft()
                visited.add(next)
                res++
            } else if (next.move(direction.turnRight()) !in edgeTiles) {
                direction = direction.turnRight()
                current = next.move(direction)
                visited.add(current.move(direction.turnRight()))
                res++
            } else {
                visited.add(next.move(direction.turnRight()))
                current = next
            }
        }while (current != actualStart || direction != startDirection)
        return res
    }

    fun makeItFat(input: List<String>): List<String> {
        val output = mutableListOf<String>()

        input.forEach {
            val stringBuilder = StringBuilder()
            it.forEach { char ->
                stringBuilder.append(char)
                stringBuilder.append(char)
            }
            output.add(stringBuilder.toString())
            output.add(stringBuilder.toString())
        }
        return output
    }
}

