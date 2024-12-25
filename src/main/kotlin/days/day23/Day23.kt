package days.day23

import days.Day
import days.day23.Day23.Connection.Companion.toJoinedString
import java.util.Queue

class Day23 : Day(false) {
    override fun partOne(): Any {
        val rawConns = readInput().map { it.split("-") }
        val resSet = mutableListOf<Set<String>>()
         rawConns.filter { it.any { it.startsWith("t") } }.forEach { conn0 ->
            val conn2 = rawConns.filter { it.contains(conn0[0]) && it != conn0 }
            val conn3 = rawConns.filter { it.contains(conn0[1]) && it != conn0 }
            val com2 = conn2.filter { conn2Connection ->
                    conn3.any { conn3Connection ->
                        conn2Connection != conn3Connection && conn2Connection != conn3Connection.reversed() &&
                        conn2Connection.any { conn2Computer ->
                            conn3Connection.any { conn3Computer ->
                                conn2Computer == conn3Computer
                            }
                        }
                    }
                }
            com2.forEach {
                val res = setOf(
                    conn0[0],
                    conn0[1],
                    it.first { it != conn0[0] && it != conn0[1] }
                )
                if (resSet.none{ it.containsAll(res) }) {
                    resSet.add(res)
                }
            }
        }

        return resSet.size
    }


    override fun partTwo(): Any {
        val connections = readInput().map(Connection::fromString)
        val vertecies: HashMap<String, List<String>> = connections.map {
            it.a to connections.filter { conn -> conn.a == it.a || conn.b == it.a }.map { conn -> if (conn.a == it.a) conn.b else conn.a }
        }.toMap() as HashMap<String, List<String>>

        val cliques = bronkerBosch(vertecies, emptySet(), vertecies.keys.toMutableSet(), mutableSetOf())

        return cliques.maxByOrNull { it.count() }!!.sorted().joinToString(",")

    }


    fun bronkerBosch(
        graph: Map<String, List<String>>,
        currentClique: Set<String>,
        remainingNodes: MutableSet<String>,
        visitedNodes: MutableSet<String>
    ): List<Set<String>> {
        if (remainingNodes.isEmpty() && visitedNodes.isEmpty()) return listOf(currentClique)
        val results = mutableListOf<Set<String>>()

        remainingNodes.toList().forEach { v ->
            val neighbours = graph[v]?.toSet() ?: emptySet()
            results.addAll(bronkerBosch(
                graph,
                currentClique + v,
                remainingNodes.intersect(neighbours).toMutableSet(),
                visitedNodes.intersect(neighbours).toMutableSet()
            ))
            remainingNodes.remove(v)
            visitedNodes.add(v)
        }
        return results
    }

    data class Connection(val a: String, val b: String) {
        fun contains(computer: String): Boolean {
            return a == computer || b == computer
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Connection
            if (a == other.a && b == other.b) return true
            if (a == other.b && b == other.a) return true
            return false
        }



        override fun toString(): String {
            return "$a-$b"
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }


        companion object {
            fun fromString(str: String): Connection {
                val split = str.split("-")
                return Connection(split[0], split[1])
            }

            fun List<Connection>.toJoinedString(): String {
                //print every loop
                val queue = this.toMutableList()
                val res = mutableListOf<MutableList<String>>(mutableListOf())

                var curr = queue.removeFirst()
                while (queue.isNotEmpty()) {
                    var next = queue.firstOrNull { it.a == curr.b || it.b == curr.b }
                    if (next == null) {
                        curr = queue.removeFirst()
                        res.add(mutableListOf())
                    } else {
                        res.last() += if (curr.a == next.a || curr.a == next.b) curr.b else curr.a
                        curr = next
                        queue.remove(next)
                    }
                }
                return res.joinToString("\n") { it.joinToString(",") }
            }
        }
    }
}

