import kotlin.math.pow

data class Card(val name: String, val numbers: Set<Int>, val winningNumbers: Set<Int>) {
    fun calculateNumberOfWinningCards(): Int {
        return numbers.fold(0) { acc, number ->
            when (number in winningNumbers) {
                true -> acc + 1
                false -> acc
            }
        }
    }

    fun calculatePoints(): Int {
        return when (val points = calculateNumberOfWinningCards()) {
            0 -> 0
            else -> 2.0.pow((points - 1).toDouble()).toInt()
        }
    }
}

data class CardWithMultiplier(val card: Card, var multiplier: Int = 1)

fun main() {
    fun parseInputLine(line: String): Card {
        val (cardName, numbersString) = line.split(": ")
        val (winningNumbersString, cardNumbersString) = numbersString.split(" | ")
        val winningNumbers =
            winningNumbersString.split(" ").map(String::trim).filter(String::isNotEmpty).map(String::toInt)
        val cardNumbers = cardNumbersString.split(" ").map(String::trim).filter(String::isNotEmpty).map(String::toInt)
        return Card(cardName, cardNumbers.toSet(), winningNumbers.toSet())
    }

    fun part1(inputLines: List<String>) = inputLines.map(::parseInputLine).sumOf(Card::calculatePoints)

    fun part2(inputLines: List<String>): Int {
        val cardsWithMultiplier = inputLines.map(::parseInputLine).map(::CardWithMultiplier)
        cardsWithMultiplier.forEachIndexed() { index, cardWithMultiplier ->
            val (card, multiplier) = cardWithMultiplier
            val numberOfWinningCards = card.calculateNumberOfWinningCards()
            for (j in index + 1..(index + numberOfWinningCards).coerceAtMost(cardsWithMultiplier.size - 1)) {
                cardsWithMultiplier[j].multiplier += multiplier
            }
        }
        return cardsWithMultiplier.sumOf { it.multiplier }
    }

    val testInput = readInputLines("Day04_test")
    println("testInput part 1 ${part1(testInput)}")
    println("testInput part 2 ${part2(testInput)}")

    val input = readInputLines("Day04")
    println("input part1 ${part1(input)}")
    println("input part2 ${part2(input)}")
}