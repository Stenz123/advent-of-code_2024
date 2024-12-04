package days.day4

import days.Day

class Day4: Day(false) {
    override fun partOne(): Any {
        val map = hashMapOf<Pair<Int, Int>, Char>()
        readInput().forEachIndexed{i, line ->
            line.forEachIndexed{ j, char ->
                map[i to j] = char
            }
        }
        fun Char.nextXmas() = when(this) {

            'X' -> 'M'
            'M' -> 'A'
            'A' -> 'S'
            else -> throw RuntimeException()
        }
        return map.filterValues { it == 'X' }.entries.sumOf { coord ->
             listOf(
                1 to 0,
                -1 to 0,
                1 to 1,
                0 to 1,
                -1 to 1,
                1 to -1,
                0 to -1,
                -1 to -1
            ).count{ dir ->
                fun Pair<Int, Int>.checkDir(char: Char, direction: Pair<Int, Int>): Boolean {
                    val nextCoord = Pair(this.first+direction.first, this.second+direction.second)

                    if (char == 'A' && map[nextCoord] == 'S'){
                        return true
                    }
                    if (map[nextCoord] == char.nextXmas()) {
                        return nextCoord.checkDir(char.nextXmas(), direction)
                    }
                    return false
                }
                coord.key.checkDir(coord.value, dir)
            }
        }

    }

    override fun partTwo(): Any {
        val map = hashMapOf<Pair<Int, Int>, Char>()
        readInput().forEachIndexed { i, line ->
            line.forEachIndexed { j, char ->
                map[i to j] = char
            }
        }
        return map.filterValues { it == 'A' }.entries.count { coord ->
            var countM = 0
            var countS = 0

            listOf(
                1 to 1,
                -1 to 1,
                1 to -1,
                -1 to -1
            ).forEach{
                when (map[Pair(coord.key.first + it.first, coord.key.second+it.second)]){
                    'M' -> countM++
                    'S' -> countS++
                }
            }

            if (countS == 2 && countM == 2) {
                if ((map[Pair(coord.key.first+1, coord.key.second+1)] == 'M' && map[Pair(coord.key.first+1, coord.key.second-1)] == 'S' && map[Pair(coord.key.first-1, coord.key.second+1)] == 'S') ||
                    (map[Pair(coord.key.first+1, coord.key.second+1)] == 'S' && map[Pair(coord.key.first+1, coord.key.second-1)] == 'M' && map[Pair(coord.key.first-1, coord.key.second+1)] == 'M')){
                    return@count false
                }
                return@count true
            }
            return@count false
        }
    }

}

