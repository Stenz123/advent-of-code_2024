package days.day2

import days.Day
import kotlin.math.absoluteValue

class Day2: Day(true) {
    override fun partOne(): Any {

        return readInput().count{line ->

            val nums = line.split(" ").map (String::toInt)



            if (!nums.equals(nums.sorted()) && !nums.equals(nums.sortedDescending())) {
                return@count false
            }

            for (i in 0 until nums.size -1) {
                if ((nums[i] - nums[i + 1]).absoluteValue !in 1..3) {
                    return@count false
                }
            }
            println(nums)
            return@count true
        }

    }

    override fun partTwo(): Any {
        return "day 2 part 2 not Implemented"

    }
}

