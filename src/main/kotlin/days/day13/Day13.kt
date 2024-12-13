package days.day13

import days.Day

class Day13: Day(false) {
    override fun partOne() = Machine.fromString(readInput()).sumOf{ machine ->
            val (aPressed, bPressed) = solveEquations(
                machine.a.first.toLong(), machine.b.first.toLong(), machine.price.first.toLong(),
                machine.a.second.toLong(), machine.b.second.toLong(), machine.price.second.toLong()
            ).takeIf { it[0] != -1L && it[1] != -1L } ?: return@sumOf 0L
            return@sumOf aPressed * 3L + bPressed
        }

    override fun partTwo() = Machine.fromString(readInput()).sumOf{ machine ->
            val (aPressed, bPressed) = solveEquations(
                machine.a.first.toLong(), machine.b.first.toLong(), machine.price.first+10000000000000,
                machine.a.second.toLong(), machine.b.second.toLong(), machine.price.second+10000000000000
            ).takeIf { it[0] != -1L && it[1] != -1L } ?: return@sumOf 0L
            return@sumOf aPressed * 3L + bPressed
        }

    private fun solveEquations(
        x1: Long, y1: Long, a1: Long,
        x2: Long, y2: Long, a2: Long
    ): LongArray {
        val det = x1 * y2 - y1 * x2
        if (det == 0L) {
            return longArrayOf(-1, -1)
        }
        val detX = a1 * y2 - y1 * a2
        val detY = x1 * a2 - a1 * x2
        if (detX % det != 0L || detY % det != 0L) {
            return longArrayOf(-1, -1)
        }
        return longArrayOf(detX / det, detY / det)
    }

    data class Machine(
        val a: Pair<Int, Int>,
        val b: Pair<Int, Int>,
        val price: Pair<Int, Int>
    ){
        companion object {
            fun fromString(str: List<String>): List<Machine> {
                val machines = mutableListOf<Machine>()
                for (i in 0..str.size step 4) {
                    val a = str[i].substringAfter("X+").substringBefore(",").toInt() to str[i].substringAfter("Y+").toInt()
                    val b = str[i+1].substringAfter("X+").substringBefore(",").toInt() to str[i+1].substringAfter("Y+").toInt()
                    val price = str[i+2].substringAfter("X=").substringBefore(",").toInt() to str[i+2].substringAfter("Y=").toInt()
                    machines.add(Machine(a, b, price))
                }
                return machines
            }
        }
    }
}