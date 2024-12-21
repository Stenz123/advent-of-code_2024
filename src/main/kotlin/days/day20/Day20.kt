package days.day20

import days.Day
import days.util.PathFinding.djikstras
import days.util.get4Neighbours
import days.util.parseToMap
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.absoluteValue

class Day20: Day(false) {
    val map = readInput().parseToMap().filter { it.value != '#' }
    val start = map.filter { it.value == 'S' }.keys.first()
    val end = map.filter { it.value == 'E' }.keys.first()
    val distancesFromEnd = distances(vertex = map.keys.toList(), start = end) {
        it.get4Neighbours().filter { it in map.keys }
    }

    val path = djikstras(map.keys.toList(), start, end){

        it.get4Neighbours().filter { it in map.keys }
    }
    override fun partOne(): Any {

       val res =  path.map { currCord ->
            currCord.reachableWhenCheating().filter { map[it] == '.' || map[it] == 'S' || map[it] == 'E'}.map { jumpedCoord ->
                (distancesFromEnd[currCord]!!-distancesFromEnd[jumpedCoord]!!) - 2
            }
                .filter { it>0 }
        }.flatten().groupingBy { it }.eachCount()
        println(res.toSortedMap())
        return res.filter { it.key >= 100 }.entries.sumOf { it.value }
    }

    fun Pair<Int, Int>.reachableWhenCheating() =
        listOf(
            this.first + 2 to this.second,
            this.first - 2 to this.second,
            this.first to this.second + 2,
            this.first to this.second - 2,

            this.first + 1 to this.second + 1,
            this.first + 1 to this.second - 1,
            this.first - 1 to this.second + 1,
            this.first - 1 to this.second - 1,

            this.get4Neighbours()
        )

fun Pair<Int, Int>.getInRadius(n: Int): List<Pair<Int, Int>> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val queue: ArrayDeque<Pair<Pair<Int, Int>, Int>> = ArrayDeque()

    // Initialize the starting position with 0 steps and enqueue it
    visited.add(this)
    queue.add(this to 0)

    val result = mutableListOf<Pair<Int, Int>>()

    // Perform BFS
    while (queue.isNotEmpty()) {
        val (current, steps) = queue.removeFirst()

        // If the number of steps exceeds n, continue
        if (steps > n) continue

        result.add(current)

        // Get neighbors of the current position

        // Enqueue unvisited neighbors
        for (neighbor in current.get4Neighbours()) {
            if (neighbor !in visited) {
                visited.add(neighbor)
                queue.add(neighbor to steps + 1)
            }
        }
    }

    return result
}

    fun stepsBetween(a: Pair<Int, Int>, b: Pair<Int, Int>) = (a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue
    override fun partTwo(): Any {
        val res =  path.map { currCord ->
            currCord.getInRadius(20).filter { map[it] == '.' || map[it] == 'S' || map[it] == 'E'}.map { jumpedCoord ->
                (distancesFromEnd[currCord]!!-distancesFromEnd[jumpedCoord]!!) - stepsBetween(currCord, jumpedCoord)
            }
                .filter { it>0 }
        }.flatten().groupingBy { it }.eachCount()
        println(res.toSortedMap())
        return res.filter { it.key >= 100 }.entries.sumOf { it.value }
    }

    fun <T> distances(vertex: List<T>, start: T, getNextVertex: (T) -> List<T>): HashMap<T, Int> {
        val dist: HashMap<T, Int> = hashMapOf()
        val queue: ArrayDeque<T> = ArrayDeque()

        // Initialize the starting vertex distance to 0 and enqueue it
        dist[start] = 0
        queue.add(start)

        // Perform BFS
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val currentDistance = dist[current] ?: continue

            // Get neighbors of the current vertex
            for (neighbor in getNextVertex(current)) {
                // If the neighbor has not been visited, calculate its distance
                if (neighbor !in dist) {
                    dist[neighbor] = currentDistance + 1
                    queue.add(neighbor)
                }
            }
        }

        return dist
    }

}

