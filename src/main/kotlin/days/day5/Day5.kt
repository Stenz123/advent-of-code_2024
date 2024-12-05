package days.day5

import days.Day
import kotlin.math.ceil
import kotlin.math.floor

class Day5: Day(false) {
    override fun partOne(): Any {
        val manual = readInput().takeWhile { it.isNotEmpty() }.map {
            val (first, second) = it.split("|").map(String::toInt)
            Pair(first, second)
        }

        return readInput().takeLastWhile { it.isNotEmpty() }.sumOf {
            val nums = it.split(",").map(String::toInt)
            val neededRules = manual.filter { (first, second) -> nums.contains(first) && nums.contains(second) }.toMutableSet()
            if (isCorrect(nums, neededRules) != null) return@sumOf 0
            return@sumOf nums[floor(nums.size/2.0).toInt()]
        }
    }

    override fun partTwo(): Any {
        val manual = readInput().takeWhile { it.isNotEmpty() }.map {
            val (first, second) = it.split("|").map(String::toInt)
            Pair(first, second)
        }

        return readInput().takeLastWhile { it.isNotEmpty() }.map { it.split(",").map(String::toInt) }.sumOf {
            val neededRules = manual.filter { (first, second) -> it.contains(first) && it.contains(second) }.toMutableSet()
            val nums = it.toMutableList()

            var incorrectRule:Pair<Int, Int>? = isCorrect(nums, neededRules) ?: return@sumOf 0
            while (incorrectRule != null) {
                nums.switchItems(nums.indexOf(incorrectRule.first), nums.indexOf(incorrectRule.second))
                incorrectRule = isCorrect(nums, neededRules)
            }
            return@sumOf nums[floor(nums.size/2.0).toInt()]
        }
    }

    private fun isCorrect(nums: List<Int>, rules: MutableSet<Pair<Int, Int>>): Pair<Int, Int>? {
        val neededRules = rules.toMutableList()
        nums.forEach{ num ->
            if (num in neededRules.map { it.second }) return neededRules.find { it.second == num }
            if (num in neededRules.map { it.first }) {
                neededRules.removeIf{it.first == num}
            }
        }
        return null
    }

    private fun MutableList<Int>.switchItems(index1: Int, index2: Int) {
        if (index1 in indices && index2 in indices) {
            val temp = this[index1]
            this[index1] = this[index2]
            this[index2] = temp
        }
    }
}

