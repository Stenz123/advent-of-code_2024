package days.day15

import days.Day
import days.util.*

class Day15: Day(false) {
    override fun partOne(): Any {
        lateinit var player: Entity
        lateinit var map: List<Entity>

        class Box(override var position: Pair<Int, Int>): Entity {
            override fun move(direction: Direction): Boolean {
                val inFront = map.firstOrNull { it.position == position.move(direction) }
                return if (inFront == null || inFront.move(direction)) {
                    position = position.move(direction)
                    true
                } else {
                    false
                }
            }
        }
        class Wall(override var position: Pair<Int, Int>): Entity {
            override fun move(direction: Direction) = false
        }

        map = readInput().takeWhile { it.isNotBlank() } .parseToMap().filter { it.value != '.' }
            .map {
                when (it.value) {
                    '#' -> Wall(it.key)
                    'O' -> Box(it.key)
                    '@' -> {
                        player = Box(it.key)
                        return@map player
                    }
                    else -> throw IllegalArgumentException("Invalid character ${it.value}")
                }
            }

        val instructions = readInput().dropWhile { it.isNotBlank() }.flatMap { it.toCharArray().toList() }
        instructions.map { it.toDirection() }.forEach { direction: Direction ->

            player.move(direction)
        }

        return map.filterIsInstance<Box>().filter{it != player}.sumOf { it.position.second + it.position.first * 100 }
    }

    override fun partTwo(): Any {
        lateinit var player: Entity
        lateinit var map: List<Entity>

        class Box(override var position: Pair<Int, Int>, var link: Box? = null, val char: Char): Entity {
            fun score(): Int {
                if (link == null) return 0
                if (link!!.position == position.west()) return 0
                return this.position.second + this.position.first * 100
            }
            override fun move(direction: Direction) = moveLink(direction)
            private fun moveLink(direction: Direction, movedByLink: Boolean = false): Boolean {
                val inFront = map.firstOrNull { it.position == position.move(direction) }


                val backup = map.associateWith { (it.position.first to it.position.second) }
                if (inFront != null && inFront == link){
                    if (this.link!!.moveLink(direction, true)) {
                        position = position.move(direction)
                        return true
                    } else {
                        return false
                    }
                }else return if (inFront == null || inFront.move(direction)) {
                    position = if (link == null) {
                        position.move(direction)
                    } else if (!movedByLink) {
                        if (this.link!!.moveLink(direction, true)) position.move(direction)
                        else {
                            map.forEach { it.position = backup[it]!! }
                            return false
                        }
                    } else {
                        position.move(direction)
                    }
                    true
                } else {
                    false
                }
            }
            override fun toString() = this.char.toString()
        }

        class Wall(override var position: Pair<Int, Int>): Entity {
            override fun move(direction: Direction) = false
            override fun toString(): String {
                return "#"
            }
        }

        map = readInput().takeWhile { it.isNotBlank() }.makeItFat().also { println(it.joinToString("\n")) }.parseToMap().filter { it.value != '.' && it.value != ']' }
            .flatMap {
                when (it.value) {
                    '#' -> listOf(Wall(it.key))
                    '[' -> {
                        val b1 = Box(it.key, null, '[')
                        val b2 = Box(it.key.east(), b1, ']')
                        b1.link = b2
                        listOf(b1, b2)
                    }
                    '@' -> {
                        player = Box(it.key, char = '@')
                        return@flatMap listOf(player)
                    }
                    else -> throw IllegalArgumentException("Invalid character ${it.value}")
                }
            }
        val instructions = readInput().dropWhile { it.isNotBlank() }.flatMap { it.toCharArray().toList() }
        var count = instructions.size
        instructions.map { it.toDirection() }.forEach { direction: Direction ->
            player.move(direction)
            //println(count--)
       //     val stringBuilder = StringBuilder()
       //     for (i in 0..map.map { it.position }.maxOf { it.first }) {
       //         for (j in 0..map.map { it.position }.maxOf { it.second }) {
       //             stringBuilder.append(map.firstOrNull { it.position == i to j }?.toString() ?: ".")
       //         }
       //         stringBuilder.append("\n")
       //     }
       //     val string = stringBuilder.toString()
       //     println(direction)
       //     println()
       //     println(string)
       //     println()

        }
        return map.filterIsInstance<Box>().sumOf { it.score() }

    }

    private fun Char.toDirection() = when (this) {
            'v' -> Direction.SOUTH
            '^' -> Direction.NORTH
            '<' -> Direction.WEST
            '>' -> Direction.EAST
            else -> throw IllegalArgumentException("Only v, ^, <, > are allowed")
        }

    interface Entity {
        var position: Pair<Int, Int>
        fun move(direction: Direction): Boolean

    }

    private fun List<String>.makeItFat(): List<String> = this.map { line ->
        line.map { char ->
            when (char) {
                'O' -> listOf('[', ']')
                '@' -> listOf('@', '.')
                else -> listOf(char, char)
            }
        }.flatten().joinToString("")
    }
}

