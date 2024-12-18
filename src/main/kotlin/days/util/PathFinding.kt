package days.util

import java.util.PriorityQueue

object PathFinding {
    fun <T> reconstructPath(cameFrom: HashMap<T, T?>, start: T, end: T): List<T> {
        val path = mutableListOf<T>()
        var current = end
        while (current != start) {
            path.add(current)
            current = cameFrom[current]!!
        }
        path.add(start)
        return path.reversed()
    }

    fun <T> djikstras(vertex: List<T>, start: T, end: T, getNextVertex: (T) -> List<T>) =
        djikstraWeighted(vertex.map { it to 1 }, start, end, getNextVertex)

    fun <T> djikstraWeighted(vertex: List<Pair<T, Int>>, start: T, end: T, getNextVertex: (T) -> List<T>): List<T> {
        val dist: HashMap<T, Int> = hashMapOf()
        val prev: HashMap<T, T?> = hashMapOf()

        for (v in vertex.map { it.first }) {
            dist[v] = Int.MAX_VALUE
            prev[v] = null
        }
        dist[start] = 0

        val pq = PriorityQueue<Pair<T, Int>>(compareBy { it.second })
        pq.add(start to 0)

        while (pq.isNotEmpty()) {
            val (u, distU) = pq.poll()
            if (u == end) break

            if (distU > dist[u]!!)
                continue

            val neighbors = getNextVertex(u)
            neighbors.forEach { neighbor ->
                val curr = vertex.first { neighbor == it.first }
                val alt = distU + curr.second
                if (alt < dist[curr.first]!!) {
                    dist[curr.first] = alt
                    prev[curr.first] = u
                    pq.add(curr.first to alt)
                }
            }
        }
        if (prev[end] == null) throw Exception("No path found")
        return reconstructPath(prev, start, end)
    }

}