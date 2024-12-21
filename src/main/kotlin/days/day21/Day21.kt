package days.day21

import days.Day
import days.util.Direction
import days.util.arrowToDirection
import days.util.move
import kotlin.math.log

/*
    +---+---+---+           +---+---+
    | 7 | 8 | 9 |           | ^ | A |
    +---+---+---+       +---+---+---+
    | 4 | 5 | 6 |       | < | v | > |
    +---+---+---+       +---+---+---+
    | 1 | 2 | 3 |
    +---+---+---+
        | 0 | A |
        +---+---+       .
*/

class Day21: Day(false) {
    override fun partOne(): Any {
        val res = robotStuff(readInput(), NumpadRobot()).map {
            println("")
            println("----")
            it.forEach { println(it) }
            robotStuff(it, DirectionRobot()).map {
                println("---------")
                it.forEach { println(it) }
                robotStuff(it, DirectionRobot()).flatten()
            }.flatten()
        }

        var ress = 0
        res.forEachIndexed { index, it ->
            ress += (it.map { it.length }.minOrNull()!! * readInput()[index].substringBefore("A").toInt())
        }

        return ress
    }

    override fun partTwo(): Any {
        return "day 21 part 2 not Implemented"
    }

    fun robotStuff(codes: List<String>, robot: Robot): List<List<String>> {
        val res = codes.map { code ->
            var numPadPaths = listOf("")
            var prevChar = 'A'
            code.map { char ->
                val newNumPadPaths = (numPadPaths).toMutableList()
                numPadPaths.forEach { mutation ->
                    newNumPadPaths.remove(mutation)
                    newNumPadPaths.addAll(robot.move(char, prevChar).map { mutation + it })
                }
                numPadPaths = newNumPadPaths
                prevChar = char
            }
            return@map numPadPaths
        }
        return res
    }

abstract class Robot{
    fun move(char: Char, oldChar: Char): List<String>{
       val currentPosition = charToCoord(oldChar)
       val newCoord = charToCoord(char)
       val y = newCoord.first - currentPosition.first
       val x = newCoord.second - currentPosition.second

       var result = ""
       result += if (x > 0) ">".repeat(x)
       else "<".repeat(-x)
       result += if (y < 0) "^".repeat(-y)
       else "v".repeat(y)


       return listOf(result, result.reversed()).filter { string ->
           var tempCoord = currentPosition!!
           var i = 0
           while (tempCoord != newCoord) {
               tempCoord = tempCoord.move(arrowToDirection(string[i]))
               i++
               if (getIllegalField() == tempCoord) return@filter false
           }
           return@filter true
       }.distinct().map { it + "A" }
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

class DirectionRobot : Robot() {
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
}

