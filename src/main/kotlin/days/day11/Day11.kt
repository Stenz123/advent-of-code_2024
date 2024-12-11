package days.day11

import com.sun.org.apache.xerces.internal.dom.ChildNode
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

    private fun blonk(stone: Pair<Long, Long>): List<Pair<Long, Long>> = when {
        stone.first == 0L -> listOf(1L to stone.second)
        stone.first.toString().length % 2 == 0 -> {
            val resString = stone.first.toString()
            listOf(resString.take(resString.length / 2).toLong() to stone.second, resString.takeLast(resString.length / 2).toLong() to stone.second)
        }
        else -> listOf(stone.first * 2024L to stone.second)
    }

    override fun partTwo(): Any {
        var stones = readInput().first().split(" ").map { it.toLong() to 1L }
        repeat(75) {
            stones = stones.map{blonk(it)}.flatten().dedupeAndSum()
        }
        return stones.sumOf { it.second }
    }
    fun List<Pair<Long, Long>>.dedupeAndSum(): List<Pair<Long, Long>> {
        return this.groupBy { it.first }
            .mapValues { (_, values) -> values.sumOf { it.second } }.toList()
    }
}

