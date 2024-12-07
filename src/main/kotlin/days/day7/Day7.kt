package days.day7

import days.Day

class Day7: Day(false) {
    override fun partOne(): Any {

        return readInput().sumOf {
            val (rawWantedSol, rawNums) = it.split(":")
            val nums = rawNums.trim().split(" ").map(String::toLong)
            val wantedSol = rawWantedSol.toLong()

            val operators = listOf('+', '*')

            makePermutations(operators, nums.size - 1).forEach { perm ->
                var result = nums[0]
                for (i in 1 until nums.size) {
                    result = when (perm[i - 1]) {
                        '+' -> result + nums[i]
                        '*' -> result * nums[i]
                        else -> throw Exception("Invalid operator")
                    }
                }
                if (result == wantedSol) {
                    return@sumOf wantedSol
                }
            }
            return@sumOf 0
        }
    }

    fun makePermutations(operators: List<Char>, n: Int): List<List<Char>> {
        if (n == 1) return operators.map { listOf(it) }
        val permutations = mutableListOf<List<Char>>()
        for (operator in operators) {
            for (perm in makePermutations(operators, n - 1)) {
                permutations.add(listOf(operator) + perm)
            }
        }
        return permutations
    }

    override fun partTwo(): Any {
        return readInput().sumOf {
            val (rawWantedSol, rawNums) = it.split(":")
            val nums = rawNums.trim().split(" ").map(String::toLong)
            val wantedSol = rawWantedSol.toLong()

            val operators = listOf('+', '*', '|')

            makePermutations(operators, nums.size - 1).forEach { perm ->
                var result = nums[0]
                for (i in 1 until nums.size) {
                    result = when (perm[i - 1]) {
                        '+' -> result + nums[i]
                        '*' -> result * nums[i]
                        '|' -> (result.toString() + nums[i].toString()).toLong()
                        else -> throw Exception("Invalid operator")
                    }
                }
                if (result == wantedSol) {
                    return@sumOf wantedSol
                }
            }
            return@sumOf 0
        }
    }
}

