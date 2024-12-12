package days.util

fun List<String>.parseToMap(): HashMap<Pair<Int, Int>, Char> {
    val map = hashMapOf<Pair<Int, Int>, Char>()
    this.forEachIndexed{i, line ->
        line.forEachIndexed{ j, char ->
            if (char != '.') {
                map[i to j] = char
            }
        }
    }
    return map
}

fun Pair<Int, Int>.get4Neighbours() = listOf(
        this.first - 1 to this.second,
        this.first to this.second - 1,
        this.first to this.second + 1,
        this.first + 1 to this.second,
    )

fun Pair<Int, Int>.get8Neighbours() = listOf(
        this.first - 1 to this.second - 1,
        this.first - 1 to this.second,
        this.first - 1 to this.second + 1,
        this.first to this.second - 1,
        this.first to this.second + 1,
        this.first + 1 to this.second - 1,
        this.first + 1 to this.second,
        this.first + 1 to this.second + 1,
    )
