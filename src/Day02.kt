data class GamePart1(val id: Int, val turns: List<String>) {
    private val maxCubes = mapOf(
        "blue" to 14,
        "green" to 13,
        "red" to 12,
    )
    val isValid: Boolean = turns.none { turn ->
        turn.split(",").map { it.trim() }.any {
            val (cubes, color) = it.split(' ')
            cubes.toInt() > maxCubes.getOrElse(color) { 0 }
        }
    }

    init {
        require(id > 0) { "id must be positive" }
        require(turns.isNotEmpty()) { "turns must not be empty" }
    }
}

data class GamePart2(val id: Int, val turns: List<String>) {
    private val minCubes = mapOf(
        "blue" to 0,
        "green" to 0,
        "red" to 0,
    )
    val power: Int = turns.flatMap { turn ->
        turn.split(",").map { it.trim() }.map {
            val (cubes, color) = it.split(' ')
            color to cubes.toInt()
        }
    }.groupingBy { it.first }
        .aggregate { _, accumulator: Int?, element, _ ->
            accumulator?.coerceAtLeast(element.second) ?: element.second
        }.values.fold(1) { acc, i -> acc * (i ?: 1) }

    init {
        require(id > 0) { "id must be positive" }
        require(turns.isNotEmpty()) { "turns must not be empty" }
    }
}

fun sumOfValidGameIds(input: List<String>): Int {
    return input.map { it.split(":") }
        .map { (idString, turns) -> GamePart1(idString.split(' ')[1].toInt(), turns.split(';')) }
        .filter { it.isValid }.sumOf { it.id }
}

fun productOfGamePowers(input: List<String>): Int {
    return input.map { it.split(":") }
        .map { (idString, turns) -> GamePart2(idString.split(' ')[1].toInt(), turns.split(';')) }
        .sumOf { it.power }
}

fun main() {
    val testInput = readInputLines("Day02_test")
    println("Test Input Part 1: ${sumOfValidGameIds(testInput)}")
    println("Test Input Part 2: ${productOfGamePowers(testInput)}")

    val input = readInputLines("Day02")
    println("Input Part 1: ${sumOfValidGameIds(input)}")
    println("Input Part 2: ${productOfGamePowers(input)}")
}
