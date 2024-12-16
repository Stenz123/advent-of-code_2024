package days.day15

import days.Day
import days.util.Direction
import days.util.move
import days.util.parseToMap

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
        class Wall(override val position: Pair<Int, Int>): Entity {
            override fun move(direction: Direction) = false
        }

        fun printMap() {
            val maxX = map.maxOf { it.position.first }
            val maxY = map.maxOf { it.position.second }
            for (x in 0..maxX) {
                for (y in 0..maxY) {
                    val entity = map.firstOrNull { it.position == Pair(x, y) }
                    print(entity?.let {
                        if (it == player) '@' else
                        when (it) {
                            is Box -> 'O'
                            is Wall -> '#'
                            else -> throw IllegalArgumentException("Invalid entity")
                        }
                    } ?: '.')
                }
                println()
            }
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
        return "day 15 part 2 not Implemented"
    }

    private fun Char.toDirection() = when (this) {
            'v' -> Direction.SOUTH
            '^' -> Direction.NORTH
            '<' -> Direction.WEST
            '>' -> Direction.EAST
            else -> throw IllegalArgumentException("Only v, ^, <, > are allowed")
        }

    interface Entity {
        val position: Pair<Int, Int>
        fun move(direction: Direction): Boolean

    }



}

