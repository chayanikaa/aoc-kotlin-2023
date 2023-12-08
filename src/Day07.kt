
enum class HandType(val value: Int, val description: String) {
    FIVE_OF_A_KIND(7, "Five of a Kind"),
    FOUR_OF_A_KIND(6, "Four of a Kind"),
    FULL_HOUSE(5, "Full House"),
    THREE_OF_A_KIND(4, "Three of a Kind"),
    TWO_PAIR(3, "Two Pair"),
    ONE_PAIR(2, "One Pair"),
    HIGH_CARD(1, "High Card");

    companion object {
        private val map = values().associateBy(HandType::value)
        fun fromValue(type: Int) = map[type]
    }
}

private data class PokerHand(val cardsString: String, val bidAmount: Long) {
    // A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
    val cardValues: List<Int> = cardsString
        .split("")
        .filter { it.isNotEmpty() }
        .map { it ->
            valuesMapPart2[it[0]]!!
        }

    val handType: Int = computeHandTypePart2(cardsString)



    override fun toString(): String {
        return "PokerHand(cards=$cardsString, bidAmount=$bidAmount, handType=$handType)"
    }

    companion object {
        val valuesMap = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 11,
            'T' to 10,
            '9' to 9,
            '8' to 8,
            '7' to 7,
            '6' to 6,
            '5' to 5,
            '4' to 4,
            '3' to 3,
            '2' to 2
        )

        val valuesMapPart2 = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'T' to 10,
            '9' to 9,
            '8' to 8,
            '7' to 7,
            '6' to 6,
            '5' to 5,
            '4' to 4,
            '3' to 3,
            '2' to 2,
            'J' to 1
        )

        val handTypeMap = mapOf(
            7 to "Five of a Kind",
            6 to "Four of a Kind",
            5 to "Full House",
            4 to "Three of a Kind",
            3 to "Two Pair",
            2 to "One Pair",
            1 to "High Card",
        )

        fun computeHandType(cardsString: String, cardValues: List<Int>): Int {
            val distinctValues = cardValues.distinct()
            val groupSizes = cardValues.groupingBy { it }.eachCount().values
            val handType = when (distinctValues.size) {
                1 -> 7 // Five of a Kind
                2 -> {
                    if (groupSizes.contains(4)) {
                        6 // Four of a Kind
                    } else {
                        // TODO check this logic
                        5
                    }

                }
                3 -> {
                    if (groupSizes.contains(3)) {
                        4 // Three of a Kind
                    } else {
                        3 // Two Pair
                    }
                }
                4 -> 2 // One Pair
                else -> 1 // High Card
            }
            return handType
        }

        fun computeHandTypePart2(cardsString: String): Int {
            val cards = cardsString.split("").filter { it.isNotEmpty() }
            val groups = cards.groupingBy { it }
            val groupSizes = groups.eachCount().values
            val maxGroup = groups.eachCount().filter { it.key != "J" }.maxByOrNull { it.value }
            if (maxGroup != null && cards.contains("J")) {
                val newCardsString = cardsString.replace("J", maxGroup.key)
                return computeHandTypePart2(newCardsString)
            }
            val handType = when (groupSizes.size) {
                1 -> 7 // Five of a Kind
                2 -> {
                    if (groupSizes.contains(4)) {
                        6 // Four of a Kind
                    } else {
                        5
                    }

                }
                3 -> {
                    if (groupSizes.contains(3)) {
                        4 // Three of a Kind
                    } else {
                        3 // Two Pair
                    }
                }
                4 -> 2 // One Pair
                else -> 1 // High Card
            }
            return handType
        }
    }
}
fun main() {
    fun parseInput(input: String): PokerHand {
        val (valuesString, bidAmountString) = input.split(" ")
        return PokerHand(valuesString.trim(), bidAmountString.trim().toLong())
    }

    fun compareHands(hand1: PokerHand, hand2: PokerHand): Int {
        var result: Int = 0
        return if (hand1.handType == hand2.handType) {
            hand1.cardValues.forEachIndexed () { index, value ->
                if (value != hand2.cardValues[index] && result == 0) {
                    result = value.compareTo(hand2.cardValues[index])
                }
            }
            result
        } else {
            hand1.handType.compareTo(hand2.handType)
        }
    }

    fun part1(input: String): Long {
        val hands = input.split("\n").map { parseInput(it) }
        val sortedHands = hands.sortedWith(::compareHands)
        val totalScore = sortedHands.mapIndexed { index, hand ->
            hand.bidAmount * (index + 1)
        }.sum()
        return totalScore
    }

//    val testInput = """
//        32T3K 765
//        T55J5 684
//        KK677 28
//        KTJJT 220
//        QQQJA 483
//    """.trimIndent()
//    println("Part 2 Test: ${part1(testInput)}")

    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
}
