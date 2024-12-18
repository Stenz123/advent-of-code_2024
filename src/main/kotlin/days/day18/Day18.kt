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
        val newTiles = readInput().map { line ->
            val split = line.split(",").map { it.toInt() }
            split[0] to split[1]
        }

        var l = 0
        var r = newTiles.size


        while (true) {
            val m = (l + r) / 2
            val tilesR = newTiles.take(m)
            try {
                PathFinding.djikstras(tiles.keys.toList(), start, end) { c ->
                    c.get4Neighbours().filter { (it.first in 0..70) && (it.second in 0..70) && it !in tilesR }
                }
                l = m + 1
            } catch (e: Exception) {
                try {
                    PathFinding.djikstras(tiles.keys.toList(), start, end) { c ->
                        c.get4Neighbours().filter { (it.first in 0..70) && (it.second in 0..70) && it !in tilesR.take(m-1) }
                    }
                    return "${newTiles[m-1].first},${newTiles[m-1].second}"
                }catch (e: Exception) { r = m - 1 }
            }
        }
    }
}

