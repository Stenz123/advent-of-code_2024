package days.util

fun Boolean.toInt() = if (this) 1 else 0

fun List<String>.parseToMap(): HashMap<Pair<Int, Int>, Char> {
    val map = hashMapOf<Pair<Int, Int>, Char>()
    this.forEachIndexed{i, line ->
        line.forEachIndexed{ j, char ->
            map[i to j] = char
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

fun arrowToDirection(arrow: Char) = when (arrow) {
    '>' -> Direction.EAST
    '<' -> Direction.WEST
    '^' -> Direction.NORTH
    'v' -> Direction.SOUTH
    else -> throw IllegalArgumentException("Invalid arrow direction")
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}

fun Direction.turnRight(): Direction {
    return when (this) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
    }
}

fun Direction.turnLeft(): Direction {
    return when (this) {
        Direction.NORTH -> Direction.WEST
        Direction.WEST -> Direction.SOUTH
        Direction.SOUTH -> Direction.EAST
        Direction.EAST -> Direction.NORTH
    }
}

fun Pair<Int, Int>.move(direction: Direction): Pair<Int, Int> {
    return when (direction) {
        Direction.NORTH -> this.first - 1 to this.second
        Direction.EAST -> this.first to this.second + 1
        Direction.SOUTH -> this.first + 1 to this.second
        Direction.WEST -> this.first to this.second - 1
    }
}
fun Pair<Int, Int>.north() = this.first - 1 to this.second
fun Pair<Int, Int>.east() = this.first to this.second + 1
fun Pair<Int, Int>.south() = this.first + 1 to this.second
fun Pair<Int, Int>.west() = this.first to this.second - 1

operator fun Pair<Int, Int>.minus(subtractor: Pair<Int, Int>) = this.first - subtractor.first to this.second - subtractor.second
operator fun Pair<Int, Int>.plus(addend: Pair<Int, Int>) = this.first + addend.first to this.second + addend.second
operator fun Pair<Int, Int>.times(multiplier: Int) = this.first * multiplier to this.second * multiplier
