package days.day22

import days.Day
import days.util.ConsoleColors

class Day22: Day(false) {
    override fun partOne(): Any {
        return readInput().map(String::toLong).sumOf {
            (0 until 2000).fold(it){ secretNumber, iterator ->
                var newSecretNumber = (secretNumber * 64L).xor(secretNumber) % 16777216L
                newSecretNumber = (newSecretNumber / 32.0).toLong().xor(newSecretNumber)
                (newSecretNumber * 2048L).xor(newSecretNumber) % 16777216L
            }
        }
    }

    override fun partTwo(): Any {
        val prices: List<List<Pair<Int, Int>>> = readInput().map(String::toLong).map {
            var secretNumber = it
            var oldSecretDigit = secretNumber.toString().takeLast(1).toInt()
            (0 until 2000).map {
                var newSecretNumber = (secretNumber * 64L).xor(secretNumber) % 16777216L
                newSecretNumber = (newSecretNumber / 32.0).toLong().xor(newSecretNumber)
                newSecretNumber = (newSecretNumber * 2048L).xor(newSecretNumber) % 16777216L
                secretNumber = newSecretNumber
                val temp = oldSecretDigit
                oldSecretDigit = secretNumber.toString().takeLast(1).toInt()
                oldSecretDigit to oldSecretDigit - temp
            }
        }

        fun calculateBananasForSequence(sequence: List<Int>) = prices.sumOf { price ->
            price.forEachIndexed { i, it ->
                if (i <= price.size - 4 && price.subList(i, i + 4).map { it.second } == sequence) {
                    return@sumOf price[i + 3].first
                }
            }
            return@sumOf 0
        }

        var maxBananas = 0
        val combinations = prices.map { price ->
            List(price.size - 3) { index ->
                price.subList(index, index + 4).map { it.second }
            }
        }.flatten().groupingBy { it }.eachCount().toList()
        val max = combinations.maxOf { it.second }
        combinations.filter { it.second > max-10 }.map { it.first }.forEachIndexed { chunkIndex, comb ->
            val bananas = calculateBananasForSequence(comb)
            if (bananas > maxBananas) {
                maxBananas = bananas
                println("Bananas: ${ConsoleColors.PURPLE}$maxBananas${ConsoleColors.RESET} | Sequence: ${ConsoleColors.PURPLE}$comb${ConsoleColors.RESET}")
            }
        }
        return maxBananas
    }

    private fun <T> List<T>.chunkEqual(chunkSize: Int): List<List<T>> {
        val chunks = List(chunkSize) { mutableListOf<T>() }
        this.forEachIndexed { index, element ->
            chunks[index % chunkSize].add(element)
        }
        return chunks.map { it.toList() }
    }
}

