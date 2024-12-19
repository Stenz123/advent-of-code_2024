package days.day19

import days.Day

class Day19: Day(false) {
    override fun partOne(): Any {
        val towels = readInput().first().split(", ").sortedBy { it.length }.reversed()
        var count = 0
        return readInput().takeLastWhile { it.isNotBlank() }.reversed().count { pattern ->
            println(count++)

            isPossible(pattern, towels.filter { it in pattern })
        }
    }

    private fun isPossible(pattern: String, towels: List<String>, cache: MutableMap<String, Boolean> = mutableMapOf()): Boolean {
        if (pattern.isEmpty()) return true
        if (cache.containsKey(pattern)) return cache[pattern]!!

        val result = towels.any { towel ->
            if (pattern.take(towel.length) == towel) {
                return@any isPossible(pattern.drop(towel.length), towels, cache)
            }
            return@any false
        }

        cache[pattern] = result
        return result
    }
    private fun isPossibleP2(pattern: String, towels: List<String>, cache: MutableMap<String, Long> = mutableMapOf()): Long {
        if (pattern.isEmpty()) return 1
        if (cache.containsKey(pattern)) return cache[pattern]!!

        val result = towels.sumOf { towel ->
            if (pattern.take(towel.length) == towel) {
                return@sumOf isPossibleP2(pattern.drop(towel.length), towels, cache)
            }
            return@sumOf 0
        }

        cache[pattern] = result
        return result
    }

    override fun partTwo(): Any {

        val towels = readInput().first().split(", ").sortedBy { it.length }.reversed()
        var count = 0
        return readInput().takeLastWhile { it.isNotBlank() }.reversed().sumOf { pattern ->
            println(count++)

            isPossibleP2(pattern, towels.filter { it in pattern })
        }

    }
}

