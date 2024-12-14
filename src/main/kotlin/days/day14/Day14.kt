package days.day14

import days.Day
import java.util.PriorityQueue
import kotlin.math.pow

class Day14: Day(false) {
    override fun partOne(): Any {
        val xRange = if (super.useExampleInput) 0 until 11 else 0 until 101
        val yRange = if (super.useExampleInput) 0 until 7 else 0 until 103

        val guards = readInput().map { Guard.fromString(it) }
        repeat(100) {
            guards.forEach { it.move(xRange, yRange) }
        }

        for (y in yRange){
            for (x in xRange){
                val count = guards.count { it.position.first == x && it.position.second == y }
                print(if (count > 0) count else ".")
            }
            println()
        }

        val q14X = if (super.useExampleInput) 6 until 11 else 51 until 101
        val q23X = if (super.useExampleInput) 0 until 5 else 0 until 50
        val q12Y = if (super.useExampleInput) 0 until 3 else 0 until 51
        val q34Y = if (super.useExampleInput) 4 until 7 else 52 until 103

        var res = (countInRange(q14X, q12Y, guards)) //q1
        res *= (countInRange(q23X, q12Y, guards)) //q2
        res *= (countInRange(q23X, q34Y, guards)) //q3
        res *= (countInRange(q14X, q34Y, guards)) //q4

        return res
    }

    override fun partTwo(): Any {
        val xRange = if (super.useExampleInput) 0 until 11 else 0 until 101
        val yRange = if (super.useExampleInput) 0 until 7 else 0 until 103

        val guards = readInput().map { Guard.fromString(it) }
        var second = 0


        fun guardsToString(): String {
            val stringBuilder = StringBuilder()
            stringBuilder.append("Second: $second\n")
            for (y in yRange) {
                for (x in xRange) {
                    if (guards.any { it.position.first == x && it.position.second == y }) {
                        stringBuilder.append("#")
                    } else {
                        stringBuilder.append(".")
                    }
                }
                stringBuilder.append("\n")
            }
            return stringBuilder.toString()
        }

        var topFormation = ""
        var minVar = Double.MAX_VALUE
        var resSec = -1
        repeat(101*103) {
            second++
            guards.forEach { it.move(xRange, yRange) }

            val xVar = guards.map { it.position.first }.variance()
            val yVar = guards.map { it.position.second }.variance()

            if (xVar * yVar < minVar) {
                minVar = xVar * yVar
                resSec = second
                topFormation = guardsToString()
            }
        }
        println(topFormation)
        return resSec
    }

    fun countInRange(xRange: IntRange, yRange: IntRange, guards: List<Guard>): Int = guards.count { it.position.first in xRange && it.position.second in yRange }

    data class Guard(var position: Pair<Int, Int>, var velocity: Pair<Int, Int>){
        fun move(xRange: IntRange, yRange: IntRange){
            position = (position.first + velocity.first to position.second + velocity.second).wrap(xRange, yRange)
        }

        companion object {
            fun fromString(input: String): Guard{
                val regex = Regex("p=\\s*([-]?\\d+),\\s*([-]?\\d+) v=\\s*([-]?\\d+),\\s*([-]?\\d+)")
                val match = regex.matchEntire(input) ?: throw RuntimeException("Invalid input")
                val (x, y, vx, vy) = match.destructured
                return Guard(x.toInt() to y.toInt(), vx.toInt() to vy.toInt())
            }
        }
    }
}

fun Pair<Int, Int>.wrap(xRange: IntRange, yRange: IntRange): Pair<Int, Int> {
    var newY: Int = this.second
    var newX: Int = this.first

    val xDiff = xRange.last - xRange.first + 1
    newX = (newX - xRange.first) % xDiff + xRange.first
    if (newX < xRange.first) newX += xDiff

    val yDiff = yRange.last - yRange.first + 1
    newY = (newY - yRange.first) % yDiff + yRange.first
    if (newY < yRange.first) newY += yDiff
    return newX to newY
}


fun List<Int>.variance(): Double {
    val mean = this.average()
    return this.sumOf { (it - mean).pow(2) } / this.size
}
