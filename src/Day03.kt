private fun getSurroundingChars(lineIndex: Int, matchResult: MatchResult, inputLines: List<String>): String {
    val (start, last) = matchResult.range.first to matchResult.range.last
    val lineLength = inputLines[lineIndex].length

    // Build the string of surrounding characters
    return buildString {
        // Previous line
        if (lineIndex > 0) {
            append(inputLines[lineIndex - 1].substring((start - 1).coerceAtLeast(0), (last + 2).coerceAtMost(lineLength)))
        }
        // Next line
        if (lineIndex < inputLines.size - 1) {
            append(inputLines[lineIndex + 1].substring((start - 1).coerceAtLeast(0), (last + 2).coerceAtMost(lineLength)))
        }
        // Current line, before and after the match
        if (start > 0) append(inputLines[lineIndex][start - 1])
        if (last < lineLength - 1) append(inputLines[lineIndex][last + 1])
    }

}
private fun isPartNumber(match: Pair<Int, MatchResult>, inputLines: List<String>): Boolean {
    val (lineIndex, matchResult) = match
    val (start, last) = matchResult.range.first to matchResult.range.last
    val lineLength = inputLines[lineIndex].length

    // Build the string of surrounding characters
    val surroundingChars = getSurroundingChars(lineIndex, matchResult, inputLines)

    return Regex("[^.0-9]").containsMatchIn(surroundingChars)
}

private data class StarNumber(val lineIndex: Int, val matchResult: MatchResult, val starCoordinates: List<Coordinate>) {

    override fun toString(): String {
        return "StarNumber(lineIndex=$lineIndex, matchResultValue=${matchResult.value}, matchResultRange=${matchResult.range}, starCoordinates=$starCoordinates)"
    }

}

private fun getGearPair(match: Pair<Int, MatchResult>, inputLines: List<String>): StarNumber {
    val (lineIndex, matchResult) = match
    val (start, last) = matchResult.range.first to matchResult.range.last
    val lineLength = inputLines[lineIndex].length
    val coordsList = mutableListOf<Coordinate>()

    for (i in (start - 1).coerceAtLeast(0)..(last + 1).coerceAtMost(lineLength - 1)) {
        if (lineIndex > 0) {
            val char1 = inputLines[lineIndex - 1][i]
            if (char1 == '*') {
                coordsList.add(Coordinate(i, lineIndex - 1))
            }
        }
        if (lineIndex < inputLines.size - 1) {
            val char2 = inputLines[lineIndex + 1][i]
            if (char2 == '*') {
                coordsList.add(Coordinate(i, lineIndex + 1))
            }
        }
    }

    if (start > 0) {
        val char = inputLines[lineIndex][start - 1]
        if (char == '*') {
            coordsList.add(Coordinate(start - 1, lineIndex))
        }
    }
    if (last < lineLength - 1) {
        val char = inputLines[lineIndex][last + 1]
        if (char == '*') {
            coordsList.add(Coordinate(last + 1, lineIndex))
        }
    }

    return StarNumber(lineIndex, matchResult, coordsList)
}

private fun part2(inputLines: List<String>): Int {
    val starNumbers = inputLines.asSequence()
        .mapIndexed { index, line -> Regex("[0-9]+").findAll(line).map { index to it } }
        .flatten()
        .filter { isPartNumber(it, inputLines) }
        .map { getGearPair(it, inputLines) }
        .toList()

    val starCoordinates = starNumbers.flatMap { it.starCoordinates }.distinct()

    return starCoordinates.fold(0) { acc, it ->
        // find if 2 star numbers have the same coordinate
        val starNumbersWithSameCoordinate = starNumbers.filter { starNumber -> starNumber.starCoordinates.contains(it) }
        if (starNumbersWithSameCoordinate.size == 2) {
            acc + starNumbersWithSameCoordinate[0].matchResult.value.toInt() * starNumbersWithSameCoordinate[1].matchResult.value.toInt()
        } else {
            acc
        }
    }
}

private fun part1(inputLines: List<String>): Int {
    return inputLines.asSequence()
        .mapIndexed { index, line -> Regex("[0-9]+").findAll(line).map { index to it } }
        .flatten()
        .filter { isPartNumber(it, inputLines) }
        .sumOf { it.second.value.toInt() }
}

fun main() {
    val testInput = readInputLines("Day03_test")
    println("Test Input Part 1: ${part1(testInput)}")
    println("Test Input Part 2: ${part2(testInput)}")

    val input = readInputLines("Day03")
    println("Input Part 1: ${part1(input)}")
    println("Input Part 2: ${part2(input)}")
}
