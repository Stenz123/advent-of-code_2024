package days.day2

import days.Day
import kotlin.math.absoluteValue

class Day2: Day(false) {
    override fun partOne(): Any {

        return readInput().count{line ->
            val nums = line.split(" ").map (String::toInt)
            if (nums != nums.sorted() && nums != nums.sortedDescending()) {
                return@count false
            }
            for (i in 0 until nums.size -1) {
                if ((nums[i] - nums[i + 1]).absoluteValue !in 1..3) {
                    return@count false
                }
            }
            return@count true
        }

    }

    override fun partTwo(): Any {
        return readInput().count{line ->
            val nums = line.split(" ").map (String::toInt)
            loop1@for (index in nums.indices) {
                val newNums = nums.toMutableList()
                newNums.removeAt(index)

                if (newNums != newNums.sorted() && newNums != newNums.sortedDescending()) {
                    continue
                }
                for (i in 0 until newNums.size -1) {
                    println(newNums[i]- newNums[i + 1])
                    if ((newNums[i] - newNums[i + 1]).absoluteValue !in 1..3) {
                        continue@loop1
                    }
                }
                return@count true
            }
            return@count false
        }
    }
}

