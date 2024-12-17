package days.day17

import days.Day
import kotlin.math.pow

class Day17: Day(false) {
    val A = readInput()[0].substringAfter("A: ").toLong()
    val B = readInput()[1].substringAfter("B: ").toLong()
    val C = readInput()[2].substringAfter("C: ").toLong()
    override fun partOne(): Any {
        val instructions = readInput()[4].substringAfter("Program: ").split(",").map { it.toInt() }
        val program = Program(A, B, C)
        return program.run(instructions).joinToString(",")
    }

    override fun partTwo(): Any {
        val instructions = readInput()[4].substringAfter("Program: ").split(",").map { it.toInt() }
        val program = Program(A, B, C)

        var a:Long = 1
        while (true) {
            try {
                Program(a, B, C).run(instructions, true).let {
                    if (it.isNotEmpty() && it == instructions) return a
                }
            }catch (_: Exception) {}
            a++
            if (a % 100000000L == 0L) println(a)
        }
    }

    class Program(
        var A: Long,
        var B: Long,
        var C: Long,
    ){
        val output = mutableListOf<Int>()
        var pointer = 0

        private var oldPointer = -1
        fun run(input: List<Int>, part2: Boolean = false): List<Int> {
            while (pointer < input.size) {
                oldPointer = pointer
                execute(input[pointer], valuePointerToValue(input[pointer + 1]))
                if (part2 && input[oldPointer] == 5) {
                    if (output.size > input.size) return listOf()
                    for (i in output.indices) {
                        if (output[i] != input[i]) return listOf()
                    }
                }
                pointer += 2
            }
            return output
        }

        private fun execute(instruction: Int, value: Long) {
            when(instruction) {
                0 -> adv(value)
                1 -> bxl(value)
                2 -> bst(value)
                3 -> jnz(value)
                4 -> bxc(value)
                5 -> out(value)
                6 -> bdv(value)
                7 -> cdv(value)
                else -> throw IllegalArgumentException("Invalid instruction: $instruction")
            }
        }

        private fun valuePointerToValue(valuePointer: Int): Long {
            return when(valuePointer) {
                in 0..3 -> valuePointer.toLong()
                4 -> A
                5 -> B
                6 -> C
                7 -> 7
                else -> throw IllegalArgumentException("Invalid valuePointer: $valuePointer")
            }
        }

        // opcode 0
        fun adv(value: Long) {
            A /= (2.0.pow(value.toDouble())).toInt()
        }

        // opcode 1
        fun bxl(value: Long) {
            B = B.xor(value.toLong())
        }

        // opcode 2
        fun bst(value: Long) {
            B = value%8L
        }

        // opcode 3
        fun jnz(value: Long) {
            if (A != 0L) {
                pointer = (value -2).toInt()
            }
        }

        // opcode 4
        fun bxc(value: Long) {
            B = C xor B
        }

        // opcode 5
        fun out(value: Long) {
            output.add((value%8).toInt())
        }

        // opcode 6
        fun bdv(value: Long) {
            B = A / (2.0.pow(value.toDouble())).toInt()
        }

        // opcode 7
        fun cdv(value: Long) {
            C = A / (2.0.pow(value.toDouble())).toInt()
        }
    }

}

