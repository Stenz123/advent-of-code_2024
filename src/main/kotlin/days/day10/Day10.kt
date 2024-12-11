package days.day10

import days.Day

class Day10: Day(false) {
    override fun partOne(): Any {
        val map = hashMapOf<Pair<Int, Int>, Int>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                if (char != '.') {
                    map[i to j] = char.digitToInt()
                }
            }
        }

        fun Pair<Int, Int>.getNeighbours(): List<Pair<Int, Int>> {
            return listOf(
                this.first - 1 to this.second,
                this.first to this.second - 1,
                this.first to this.second + 1,
                this.first + 1 to this.second,
            ).filter { map.containsKey(it) }
        }

        val reached = mutableSetOf<Pair<Int, Int>>()
        fun Pair<Int, Int>.hike() {
            if (map[this] == 9){
                reached.add(this)
                return
            }
            val neighbours = this.getNeighbours().filter { map[it] == map[this]!!+1 }
            neighbours.forEach { it.hike() }
        }

        return map.filter { it.value == 0 }.keys.sumOf{
            reached.clear()
            it.hike()
            reached.size
        }
    }

    override fun partTwo(): Any {
        val map = hashMapOf<Pair<Int, Int>, Int>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                if (char != '.') {
                    map[i to j] = char.digitToInt()
                }
            }
        }

        fun Pair<Int, Int>.getNeighbours(): List<Pair<Int, Int>> {
            return listOf(
                this.first - 1 to this.second,
                this.first to this.second - 1,
                this.first to this.second + 1,
                this.first + 1 to this.second,
            ).filter { map.containsKey(it) }
        }

        fun Pair<Int, Int>.hike():Int {
            if (map[this] == 9)return 1
            val neighbours = this.getNeighbours().filter { map[it] == map[this]!!+1 }
            return neighbours.sumOf { it.hike() }
        }

        return map.filter { it.value == 0 }.keys.sumOf{
            it.hike()
        }
    }
}

