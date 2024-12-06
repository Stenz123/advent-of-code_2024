package days.day6

import days.Day
import kotlinx.coroutines.*
import kotlin.math.log

class Day6: Day(false) {
    override fun partOne(): Any {
        val map = hashMapOf<Pair<Int, Int>, Char>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                map[i to j] = char
            }
        }

        fun Pair<Int, Int>.getNext(direction: Pair<Int, Int>): MutableMap.MutableEntry<Pair<Int, Int>, Char>? =map.entries.find { it.key == Pair(this.first+direction.first, this.second+direction.second) }

        var guardPosition = map.entries.find { it.value == '^' }!!.key
        var guardDirection = -1 to 0

        map[guardPosition] = 'X'

        while (true) {
            val next = guardPosition.getNext(guardDirection)?: return map.values.count{it=='X'}

            if (next.value == '#') guardDirection = guardDirection.turn90Deg()
            else {
                guardPosition = next.key
                map[guardPosition] = 'X'
            }
        }
    }

fun Pair<Int, Int>.turn90Deg() = when (this) {
    1 to 0 -> 0 to -1
    0 to 1 -> 1 to 0
    -1 to 0 -> 0 to 1
    0 to -1 -> -1 to 0
    else -> { throw RuntimeException() }
}

    override fun partTwo(): Any {

        val map = hashMapOf<Pair<Int, Int>, Char>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                map[i to j] = char
            }
        }

        fun Pair<Int, Int>.getNext(direction: Pair<Int, Int>): MutableMap.MutableEntry<Pair<Int, Int>, Char>? =map.entries.find { it.key == Pair(this.first+direction.first, this.second+direction.second) }

        var guardPosition = map.entries.find { it.value == '^' }!!.key
        var guardDirection = -1 to 0

        while (true) {
            val next = guardPosition.getNext(guardDirection)?: break

            if (next.value == '#') guardDirection = guardDirection.turn90Deg()
            else {
                guardPosition = next.key
                map[guardPosition] = 'X'
            }
        }
        println("here")
        val map2 = map.toMutableMap()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                map2[i to j] = char
            }
        }

        var row = 0

        return runBlocking {
            map.filter { it.value == 'X' }.count {
                println(row++)
                runBlocking {
                    withContext(Dispatchers.Default) {
                        simulateLoop(map2.toMutableMap().apply { this[it.key] = '#' })
                    }
                }
            }
        }
    }

    fun simulateLoop(map: MutableMap<Pair<Int, Int>, Char>): Boolean {
        fun Pair<Int, Int>.getNext(direction: Pair<Int, Int>): MutableMap.MutableEntry<Pair<Int, Int>, Char>? = map.entries.find { it.key == Pair(this.first+direction.first, this.second+direction.second) }
        val visitedPostitions: MutableSet<Pair<Pair<Int, Int>,Pair<Int, Int>>> = mutableSetOf()
        var guardPosition = map.entries.find { it.value == '^' }?.key
        if (guardPosition == null) return false
        var guardDirection = -1 to 0

        while (true) {
            val next = guardPosition!!.getNext(guardDirection)?: return false

            if (next.value == '#') guardDirection = guardDirection.turn90Deg()
            if (visitedPostitions.contains(next.key to guardDirection)) {
                println("loop")
                return true
            }
            if (next.value != '#') {
                guardPosition = next.key
                visitedPostitions.add(guardPosition to guardDirection)
            }
        }
    }
}

