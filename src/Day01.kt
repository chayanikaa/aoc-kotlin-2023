fun main() {
    val textNumbersMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    fun findAllDigits(input: String): List<Int> {
        val processedInput = input.replace(Regex("one|two|three|four|five|six|seven|eight|nine")) {
            textNumbersMap[it.value]!!.toString()
        }
        return processedInput.filter(Char::isDigit).map(Char::digitToInt)
    }

    fun part1(input: List<String>): Int {
        val digits = input.map { it.filter(Char::isDigit) }
        val numbers = digits.map{ it.first().digitToInt() * 10 + it.last().digitToInt() }
        return numbers.sum()
    }

    fun part2(input: List<String>): Int {
        val digits = input.map { findAllDigits(it) }
        val numbers = digits.map{ it.first() * 10 + it.last() }
        return numbers.sum()
    }

    val testInput = readInputLines("Day01_test")
    // println("testInput ${part1(testInput)}")
    println("testInput ${part2(testInput)}")

    val input = readInputLines("Day01")
     part1(input).println()
     part2(input).println()
}
