package days.day1

import days.Day
import java.util.PriorityQueue
import kotlin.math.absoluteValue

class Day1: Day(true) {
    override fun partOne(): Any {
        val input = readInput()
        val (l1, l2) = input.map { line ->
            val split = line.split(" ").filter(String::isNotBlank).map(String::toLong)
            split[0] to split[1]
        }.unzip()
        return l1.sorted().zip(l2.sorted()){a, b -> (a-b).absoluteValue}.sum()
    }

    override fun partTwo(): Any {
        val input = readInput()
        val (l1, l2) = input.map { line ->
            val split = line.split(" ").filter(String::isNotBlank).map(String::toLong)
            split[0] to split[1]
        }.unzip()
        return l1.sumOf { item ->
            item * l2.count { it == item }
        }
    }
}

