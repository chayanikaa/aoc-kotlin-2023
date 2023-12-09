fun main() {
    fun parseInput(input: List<String>): List<List<Long>> {
        return input.map { it.split(" ").map(String::toLong) }
    }

    fun findDifferences(sequence: List<Long>): MutableList<Long> {
        val differenceSequence = mutableListOf<Long>()
        sequence.forEachIndexed { index, value ->
            if (index > 0) {
                differenceSequence.add(value - sequence[index - 1])
            }
        }
        return differenceSequence
    }

    fun mapSequenceToDifferences(sequence: List<Long>): MutableList<List<Long>> {
        val differences: MutableList<List<Long>> = mutableListOf(sequence)
        var index = 0
        while(differences[index].distinct().size != 1 || differences[index][0] != 0L) {
            differences.add(findDifferences(differences[index]))
            index++
        }
        return differences
    }

    // todo try tailrec here
    fun part1(input: List<String>): Long {
        val sequences = parseInput(input)
        return sequences.sumOf { it ->
            val differences = mapSequenceToDifferences(it)
            val reversedDifferences = differences.reversed()
            val lastExtrapolatedList: MutableList<Long> = mutableListOf(0L)
            var diffIndex = 1
            while (diffIndex < reversedDifferences.size) {
                val currentDiff = reversedDifferences[diffIndex]
                val prevValue = lastExtrapolatedList[diffIndex - 1]
                lastExtrapolatedList.add(currentDiff[currentDiff.size - 1] + prevValue)
                diffIndex++
            }
            lastExtrapolatedList[lastExtrapolatedList.size - 1]
        }
    }

    fun part2(input: List<String>): Long {
        val sequences = parseInput(input)
        return sequences.sumOf { it ->
            val differences = mapSequenceToDifferences(it)
            val reversedDifferences = differences.reversed()
            val firstExtrapolatedList: MutableList<Long> = mutableListOf(0L)
            var diffIndex = 1
            while (diffIndex < reversedDifferences.size) {
                val currentDiff = reversedDifferences[diffIndex]
                val prevValue = firstExtrapolatedList[diffIndex - 1]
                firstExtrapolatedList.add(currentDiff[0] - prevValue)
                diffIndex++
            }
            firstExtrapolatedList[firstExtrapolatedList.size - 1]
        }
    }

    val testInput = """0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45"""
     println("testInput part1 ${part1(testInput.split("\n"))}")
     println("testInput part2 ${part2(testInput.split("\n"))}")

     val input = readInputLines("Day09")
      println("part1 ${part1(input)}")
     println("part2 ${part2(input)}")
}
