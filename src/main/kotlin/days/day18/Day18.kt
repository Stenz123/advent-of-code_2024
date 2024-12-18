package days.day18

import days.Day
import days.util.ConsoleColors
import days.util.PathFinding
import days.util.get4Neighbours

class Day18: Day(false) {
    override fun partOne(): Any {
        
        val numberOfTiles = if (super.useExampleInput) 12 else 1024

        val tiles = mutableMapOf<Pair<Int, Int>, Boolean>()
        val start = 0 to 0
        val end = if (super.useExampleInput) 6 to 6 else 70 to 70
        for (i in 0..end.first) {
            for (j in 0..end.second) {
                tiles[i to j] = tiles.getOrDefault(i to j, false)
            }
        }
        readInput().take(numberOfTiles).forEach { line ->
            val split = line.split(",").map { it.toInt() }
            tiles[split[0] to split[1]] = true
        }

        val path = PathFinding.djikstras(tiles.keys.toList(), start, end) { c ->
            c.get4Neighbours().filter { it in tiles && tiles[it] == false }
        }

        for (j in 0..end.first) {
            for (i in 0..end.second) {
                if (i to j in path) print("${ConsoleColors.GREEN}O${ConsoleColors.RESET}")
                else print(tiles[i to j]?.let { if (it) "#" else "." } ?: " ")
            }
            println()
        }

        return path.size - 1
    }

    override fun partTwo(): Any {
        val tiles = mutableMapOf<Pair<Int, Int>, Boolean>()
        val start = 0 to 0
        val end = if (super.useExampleInput) 6 to 6 else 70 to 70
        for (i in 0..end.first) {
            for (j in 0..end.second) {
                tiles[i to j] = tiles.getOrDefault(i to j, false)
            }
        }
        readInput().forEachIndexed { index, line ->
            val split = line.split(",").map { it.toInt() }
            tiles[split[0] to split[1]] = true
            println(index)
            try {
                if (index > 1024) {
                    PathFinding.djikstras(tiles.keys.toList(), start, end) { c ->
                        c.get4Neighbours().filter { it in tiles && tiles[it] == false }
                    }
                }
            } catch (e: Exception) {
                return "${split[0]},${split[1]}"
            }
        }

        return "day 18 part 2 not Implemented"
    }
}

