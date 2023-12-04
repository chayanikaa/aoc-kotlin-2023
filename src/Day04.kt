import kotlin.math.pow

data class Card(val name: String, val numbers: Set<Int>, val winningNumbers: Set<Int>) {
    fun calculateNumberOfWinningCards(): Int {
        var numberOfWinningCards = 0
        for (number in numbers) {
            if (number in winningNumbers) {
                numberOfWinningCards += 1
            }
        }
        return numberOfWinningCards
    }

    fun calculatePoints(): Int {
        val points = calculateNumberOfWinningCards()
        if (points == 0) {
            return 0
        }
        return 2.0.pow((points - 1).toDouble()).toInt()
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

    fun part1(inputLines: List<String>): Int {
        val cards = inputLines.map(::parseInputLine)
        return cards.map(Card::calculatePoints).sum()
    }

    fun part2(inputLines: List<String>): Int {
        val cards = inputLines.map(::parseInputLine)
        val cardsWithMultiplier = cards.map(::CardWithMultiplier)
        cardsWithMultiplier.forEachIndexed() { index, cardWithMultiplier ->
            val (card, multiplier) = cardWithMultiplier
            val numberOfWinningCards = card.calculateNumberOfWinningCards()
            for (j in index + 1..index + numberOfWinningCards) {
                cardsWithMultiplier[j].multiplier += multiplier
            }
        }
        return cardsWithMultiplier.map { it.multiplier }.sum()
    }

    val testInput = readInputLines("Day04_test")
    println("testInput part 1 ${part1(testInput)}")
    println("testInput part 2 ${part2(testInput)}")

    val input = readInputLines("Day04")
    println("input part1 ${part1(input)}")
    println("input part2 ${part2(input)}")
}