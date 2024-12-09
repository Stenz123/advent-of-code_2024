package days.day9

import days.Day
import kotlin.math.log

class Day9: Day(false) {
    override fun partOne(): Any {
        val input = parseInput()

        var i = 0

        while (i < input.size) {
            if (input[i] == null) {
                input.indexOfLast { it != null }.let { index ->
                    input[i] = input[index]
                    input[index] = null
                    input.removeLastWhile{ it == null }
                }
            }
            i++
        }

        var res:Long = 0
        input.forEachIndexed { index, value ->
            if (value != null){
                res += value*index
            }
        }

        return res
    }

    override fun partTwo(): Any {
        val input = parseInput()

        for (currId in input.maxBy { it ?: -1 }!! downTo 0) {
            val slot = mutableListOf<Int>()
            var iterator = input.indexOfLast { it == currId }
            if (iterator == -1) continue
            while (iterator >= 0 && input[iterator] == currId) {
                slot.add(iterator)
                iterator--
            }
            println(currId)
            for (i in 0 until slot.minOf { it }) {
                if (input[i] == null && input.drop(i).take(slot.size).all { it == null }) {
                    for (j in i until i + slot.size) {
                        input[j] = currId
                    }
                    slot.forEach { input[it] = null }
                    break
                }
            }
        }


        var res:Long = 0
        input.forEachIndexed { index, value ->
            if (value != null){
                res += value*index
            }
        }

        return res
    }

    private fun parseInput(): MutableList<Int?> {
        var currId = -1
        var isFreeBlock = true

        return readInput().first().map(Char::digitToInt).map {
            isFreeBlock = !isFreeBlock
            if (!isFreeBlock) currId++
            if (isFreeBlock) List(it){null} else List(it) {currId}
        }.flatten().toMutableList()
    }

    private fun <E> MutableList<E>.removeLastWhile(predicate: (E) -> Boolean) {
        while (predicate(last())) {
            removeLast()
        }
    }
}

