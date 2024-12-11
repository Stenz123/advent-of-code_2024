package days.day11

import days.Day

class Day11: Day(false) {
    override fun partOne(): Any {

        var stones = readInput().first().split(" ").map { it.toLong() }
        var count = 0
        repeat(25) {
            println(count++)
            stones = stones.blink()
        }
        return stones.size
    }

    private fun List<Long>.blink(): List<Long> {
        val res = mutableListOf<Long>()
        this.forEach {
            if (it == 0L) {
                res.add(1L)
            } else if (it.toString().length % 2 == 0) {
                val resString = it.toString()
                res.add(resString.take(resString.length / 2).toLong())
                res.add(resString.takeLast(resString.length / 2).toLong())
            } else {
                res.add(it*2024)
            }
        }
        return res
    }

    override fun partTwo(): Any {
        return "day 11 part 2 not Implemented"
    }
}

