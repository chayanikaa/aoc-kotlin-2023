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
        while(differences[index].all { it != 0L }) {
            differences.add(findDifferences(differences[index]))
            index++
        }
        return differences
    }

    tailrec fun calculateNextSequence(differences: List<Long>, lastDifference: Long = 0L): Long {
        if (differences.all { it == 0L }) {
            return lastDifference
        }
        val nextLevelDifferences = findDifferences(differences)
        return calculateNextSequence(nextLevelDifferences, lastDifference + differences.last())
    }

    // this is better but not tail recursive
    fun calculatePreviousSequence(differences: List<Long>): Long {
        if (differences.all { it == 0L }) {
            return 0L
        }
        val nextLevelDifferences = findDifferences(differences)
        return differences.first() - calculatePreviousSequence(nextLevelDifferences)
    }

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

    // added afterwards, the nicer version
    fun part1Rec(input:List<String>): Long {
        val sequences = parseInput(input)
        return sequences.sumOf { calculateNextSequence(it) }
    }

    fun part2Rec(input:List<String>): Long {
        val sequences = parseInput(input)
        return sequences.sumOf { calculatePreviousSequence(it) }
    }

    val testInput = """0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45"""
     println("testInput part1 ${part1Rec(testInput.split("\n"))}")
     println("testInput part2 ${part2Rec(testInput.split("\n"))}")
//
     val input = readInputLines("Day09")
       println("part1Rec ${part1Rec(input)}")
       println("part2 ${part2Rec(input)}")
}
