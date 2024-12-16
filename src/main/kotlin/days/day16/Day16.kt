package days.day16

import com.sun.jmx.remote.internal.ArrayQueue
import days.Day
import days.util.*
import java.sql.Array
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.HashMap

class Day16: Day(true) {
    val map = readInput().parseToMap().filter { it.value == '.' || it.value == 'S' || it.value == 'E' }
    val start = map.filter { it.value == 'S' }.keys.first()
    val end = map.filter { it.value == 'E' }.keys.first()
    override fun partOne() = djikstraWeighted(map.keys.toList(), start, end, cost = true)

    override fun partTwo(): Any {

        fun printMap(tiles: Set<Pair<Int, Int>>) {
            for (i in 0..map.keys.maxOf { it.first } + 1) {
                for (j in 0..map.keys.maxOf { it.second } + 1) {
                    if (tiles.contains(i to j)) {
                        print(ConsoleColors.GREEN + "0" + ConsoleColors.RESET)
                    } else if (map.keys.contains(i to j)) {
                        print(map[i to j])
                    } else {
                        print("#")
                    }
                }
                println()
            }
        }

        val cost = djikstraWeighted(map.keys.toList(), start, end, cost = true) as Int
        val initialTiles = djikstraWeighted(map.keys.toList(), start, end) as  List<Pair<Int, Int>>
        val blockingTiles: PriorityQueue<List<Pair<Int, Int>>> = PriorityQueue(compareBy{it.size})
        blockingTiles.addAll(initialTiles.filter { it.get4Neighbours().count{ it in map } > 2 }.map { listOf(it)})
        val finalTiles = blockingTiles.peek().toMutableSet()

        while (blockingTiles.isNotEmpty()) {
            val tileToRemove = blockingTiles.poll()
            val newTiles = (djikstraWeighted(map.keys.filter { it !in tileToRemove  }, start, end, cost) as List<Pair<Int, Int>>)
                .filter { finalTiles.add(it) && it.get4Neighbours().count{ it in map} > 2}
            blockingTiles.addAll(newTiles.flatMap { listOf(tileToRemove + it) })
        }
        printMap(finalTiles)

        return finalTiles.size
    }

    fun djikstraWeighted(vertex: List<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>, maxCost: Int? = null, cost: Boolean = false): Any {
        val dist: HashMap<Pair<Int, Int>, Int> = hashMapOf()
        val prev: HashMap<Pair<Int, Int>, Pair<Int, Int>?> = hashMapOf()

        for (v in vertex) {
            dist[v] = Int.MAX_VALUE
            prev[v] = null
        }
        dist[start] = 0
        prev[start] = start.west()

        val pq = PriorityQueue<Pair<Pair<Int, Int>, Int>>(compareBy { it.second })
        pq.add(start to 0)

        while (pq.isNotEmpty()) {
            val (u, distU) = pq.poll()
            if (u == end) break

            if (distU > dist[u]!!)
                continue

            val front = if (prev[u] != null) listOf(u + ((u - prev[u]!!) * 1)) to 1 else u.get4Neighbours() to 1
            val neighbors = u.get4Neighbours().minus(front.first.toSet()).minus(prev[u]) to 1001

            val nexts: List<Pair<Pair<Int, Int>, Int>> = listOf(front, neighbors).filter { it.first.isNotEmpty() }
                .flatMap { map -> map.first.map { it!! to map.second } }

            nexts.forEach { next ->
                if (vertex.contains(next.first)) {
                    val alt = distU + next.second
                    if (maxCost == null || alt <= maxCost) {
                        if (alt < dist[next.first]!!) {
                            dist[next.first] = alt
                            prev[next.first] = u
                            pq.add(next.first to alt)
                        }
                    }
                }
            }
        }
        if (cost) {
            return dist[end]!!
        }

        if (prev[end] == null) return emptyList<Pair<Int, Int>>()

        val path = mutableListOf<Pair<Int, Int>>()
        var current = end
        while (current != start) {
            path.add(current)
            current = prev[current]!!
        }
        path.add(start)
        return path.reversed()
    }
}


