package days.day8

import days.Day

class Day8: Day(false) {
    override fun partOne(): Any {
        val map = hashMapOf<Pair<Int, Int>, Char>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                if (char != '.') map[i to j] = char
            }
        }

        val antiNodes: Set<Pair<Int, Int>> =
            map.values.toSet().map { char ->
            val nodes = map.filter { it.value == char }.keys
            nodes.flatMap { node -> nodes.map {
                it to node
            }.filter { it.first != it.second } }
                .map { antiNodeLocation(it) }.flatten()
        }.flatten().toSet()

        return antiNodes.filter { it.first in 0 until readInput().size && it.second in 0 until readInput()[0].length }.size
    }

    fun antiNodeLocation(nodes: Pair<Pair<Int, Int>, Pair<Int, Int>>): List<Pair<Int, Int>> {
        val distance = nodes.first.first - nodes.second.first to nodes.first.second - nodes.second.second
        val antiNode1 = nodes.first.first + distance.first to nodes.first.second + distance.second
        val antiNode2 = nodes.second.first - distance.first to nodes.second.second - distance.second
        return listOf(antiNode1, antiNode2)
    }

    fun infiniteAntiNodeLocation(nodes: Pair<Pair<Int, Int>, Pair<Int, Int>>, size: Int): List<Pair<Int, Int>> {
        val distance = nodes.first.first - nodes.second.first to nodes.first.second - nodes.second.second

        val result = mutableListOf<Pair<Int, Int>>()

        var antiNode1 = nodes.first.first + distance.first to nodes.first.second + distance.second
        var antiNode2 = nodes.second.first - distance.first to nodes.second.second - distance.second

        while (antiNode1.first in 0 until size && antiNode1.second in 0 until size) {
            result.add(antiNode1)
            antiNode1 = antiNode1.first + distance.first to antiNode1.second + distance.second
        }
        while (antiNode2.first in 0 until size && antiNode2.second in 0 until size) {
            result.add(antiNode2)
            antiNode2 = antiNode2.first - distance.first to antiNode2.second - distance.second
        }
        return result
    }

    override fun partTwo(): Any {
        val map = hashMapOf<Pair<Int, Int>, Char>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                if (char != '.') map[i to j] = char
            }
        }

        val antiNodes: MutableSet<Pair<Int, Int>> =
            map.values.toSet().map { char ->
                val nodes = map.filter { it.value == char }.keys
                nodes.flatMap { node -> nodes.map {
                    it to node
                }.filter { it.first != it.second } }
                    .map { infiniteAntiNodeLocation(it, readInput().size) }.flatten()
            }.flatten().toMutableSet()

        //println(antiNodes)
        for (i in 0 until readInput().size) {
            for (j in 0 until readInput()[0].length) {
                if (i to j in map) print(map[i to j])
                else if (i to j in antiNodes) print("#")
                else print(".")
            }
            println()
        }

        antiNodes.addAll(map.keys)

        return antiNodes.size
    }
}

