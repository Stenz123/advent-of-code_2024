package days.day25

import days.Day

class Day25: Day(false) {
    override fun partOne(): Any {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()

        val rawInput = readInput()
        for (i in rawInput.indices step 8) {
            val subList = rawInput.subList(i + 1, i + 6).rotate().map { it.count { c -> c == '#' } }
            if (rawInput[i][0] == '#') locks.add(subList)
            else keys.add(subList)
        }

        return locks.sumOf { lock ->
            keys.count { key ->
                fit(key, lock)
            }
        }
    }

    override fun partTwo(): Any {
        return "50 Starts YAAAAYYYYY!!!!!!!!!"
    }

    fun fit(lock: List<Int>, key: List<Int>): Boolean {
        for (i in key.indices) {
            if (key[i] + lock[i] > 5) return false
        }
        return true
    }

    private fun List<String>.rotate(): List<String> {
        return (0 until this[0].length).map { col ->
            (this.size - 1 downTo 0).joinToString("") { row -> this[row][col].toString() }
        }
    }
}

