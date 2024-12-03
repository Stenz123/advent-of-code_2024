package days.day3

import days.Day

class Day3: Day(false) {
    override fun partOne(): Any {
        return readInput().sumOf {
            Regex("""mul\(\d+,\d+\)""").findAll(it).sumOf {
                it.value.substringAfter("mul(").substringBefore(")").split(",").map(String::toInt).reduce(Int::times)
            }
        }
    }

    override fun partTwo(): Any {
        var currentMultiplier = 1
        return readInput().sumOf {
            Regex("""(mul\(\d+,\d+\)|don't\(\)|do\(\))""").findAll(it).map { it.value }.sumOf {
                currentMultiplier = if (it == "don't()") 0
                else if (it == "do()") 1
                else return@sumOf it.substringAfter("mul(").substringBefore(")").split(",").map(String::toInt).reduce(Int::times) * currentMultiplier
                0
            }
        }
    }
}

