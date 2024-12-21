package days.day21

import days.Day
import days.util.arrowToDirection
import days.util.move
import kotlin.math.max

/*
    +---+---+---+           +---+---+
    | 7 | 8 | 9 |           | ^ | A |
    +---+---+---+       +---+---+---+
    | 4 | 5 | 6 |       | < | v | > |
    +---+---+---+       +---+---+---+
    | 1 | 2 | 3 |
    +---+---+---+
        | 0 | A |
        +---+---+
*/

class Day21: Day(false) {
    val cache = mutableMapOf<Triple<String, Int, Int>, Long>()
    override fun partOne(): Any {
        return readInput().sumOf{
            println("${recursiveRobotStuff(0, 2, it)} * ${it.substringBefore('A').toInt()}")
            recursiveRobotStuff(0, 2, it) * it.substringBefore('A').toInt()
        }
    }

    override fun partTwo(): Any {



        return readInput().sumOf{
            println("${recursiveRobotStuff(0, 2, it)} * ${it.substringBefore('A').toInt()}")
            recursiveRobotStuff(0, 25, it) * it.substringBefore('A').toInt()
        }
    }

    fun recursiveRobotStuff(depth: Int, maxDepth: Int, sequence: String): Long {
        val key = Triple(sequence, depth, maxDepth)
        return cache.getOrPut(key) {
            val robot = if (depth == 0) NumpadRobot() else DirectionRobot()
            sequence.fold('A' to 0L) { (prevChar ,sum), char ->
                val paths = robot.move(char, prevChar)
                char to (sum + if (depth == maxDepth) {
                    paths.minOfOrNull { it.length }?.toLong() ?: 0L
                } else {
                    paths.minOfOrNull { recursiveRobotStuff(depth + 1, maxDepth, it) }?.toLong() ?: 0L
                })
            }.second
        }
    }



    fun getPaths(code: String, robot: Robot): List<String> {
        var numPadPaths = listOf("")
        var prevChar = 'A'
         code.forEach { char ->

            val newNumPadPaths = (numPadPaths).toMutableList()
            numPadPaths.forEach { mutation ->
                newNumPadPaths.remove(mutation)
                newNumPadPaths.addAll(robot.move(char, prevChar).map { mutation + it })
            }
            numPadPaths = newNumPadPaths
            prevChar = char
        }
        return numPadPaths
    }

    abstract class Robot() {

    fun move(char: Char, oldChar: Char): List<String> {
        val currentPosition = charToCoord(oldChar)
        val newCoord = charToCoord(char)
        val y = newCoord.first - currentPosition.first
        val x = newCoord.second - currentPosition.second

        var result = ""
        result += if (x > 0) ">".repeat(x) else "<".repeat(-x)
        result += if (y < 0) "^".repeat(-y) else "v".repeat(y)

        val res = listOf(result, result.reversed()).filter { string ->
            var tempCoord = currentPosition
            var i = 0
            while (tempCoord != newCoord) {
                tempCoord = tempCoord.move(arrowToDirection(string[i]))
                i++
                if (getIllegalField() == tempCoord) return@filter false
            }
            true
        }.distinct().map { it + "A" }

        return res
    }

    fun stringToCoord(dirs: String): Pair<Int, Int> {
        val dirs = dirs.groupingBy { it }.eachCount()
        val final = charToCoord('A')
        if ((dirs['>'] ?: 0) >= (dirs['<'] ?: 0)) final.second + (dirs['>']?:0)
        else final.second - (dirs['>']?:0)
        if ((dirs['^'] ?: 0) >= (dirs['v'] ?: 0)) final.first + (dirs['^']?:0)
        else final.second - (dirs['v']?:0)
        return final
    }

   abstract fun getIllegalField(): Pair<Int, Int>
   abstract fun charToCoord(char: Char): Pair<Int, Int>
}

    class DirectionRobot() : Robot() {
    override fun getIllegalField() = 0 to 0

    override fun charToCoord(char: Char): Pair<Int, Int> = when (char) {
        '^' -> 0 to 1
        '<' -> 1 to 0
        'v' -> 1 to 1
        '>' -> 1 to 2
        'A' -> 0 to 2
        else -> throw RuntimeException()
    }
}

//

class NumpadRobot(): Robot(){
    override fun getIllegalField() = 3 to 0

    override fun charToCoord(char: Char): Pair<Int, Int> = when (char) {
        '7' -> 0 to 0
        '8' -> 0 to 1
        '9' -> 0 to 2
        '4' -> 1 to 0
        '5' -> 1 to 1
        '6' -> 1 to 2
        '1' -> 2 to 0
        '2' -> 2 to 1
        '3' -> 2 to 2
        '0' -> 3 to 1
        'A' -> 3 to 2
        else -> throw RuntimeException("Invalid character")
    }
}
    data class CachingState(
        val current: Char,
        val destination: Char,
        val numberOfRobots: Int,
    )
}

