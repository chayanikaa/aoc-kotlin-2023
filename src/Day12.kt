private fun satisfiesPartialContiguousBrokenCondition(inputLine: String, brokenGroups: List<Int>): Boolean {
    var toMatchI = 0
    var cumulativeBrokenCount = 0
    for (index in inputLine.indices) {
        val char = inputLine[index]
        when (char) {
            '#' -> {
                cumulativeBrokenCount++
                if (cumulativeBrokenCount > brokenGroups[toMatchI]) {
                    return false
                }
            }
            '.' -> {
                if (cumulativeBrokenCount == brokenGroups[toMatchI]) {
                    cumulativeBrokenCount = 0
                    toMatchI++
                }
            }
        }
    }
    return true
}

private fun satisfiesFullContiguousBrokenCondition(inputLine: String, brokenGroups: List<Int>): Boolean {
    var toMatchI = 0
    var cumulativeBrokenCount = 0
    for (index in inputLine.indices) {
        val char = inputLine[index]

        when (char) {
            '#' -> {
                cumulativeBrokenCount++
                if (toMatchI == brokenGroups.size || cumulativeBrokenCount > brokenGroups[toMatchI]) {
                    return false
                }
            }
            '.' -> {
                // println("matched . index $index $inputLine cumulativeBrokenCount $cumulativeBrokenCount toMatchI $toMatchI")
                if (toMatchI < brokenGroups.size && cumulativeBrokenCount > 0 && cumulativeBrokenCount != brokenGroups[toMatchI]) {
                    return false
                } else if (toMatchI < brokenGroups.size && cumulativeBrokenCount == brokenGroups[toMatchI]) {
                    toMatchI++
                }
                cumulativeBrokenCount = 0
            }
        }
    }
    if (toMatchI < brokenGroups.size && cumulativeBrokenCount == brokenGroups[toMatchI]) {
        cumulativeBrokenCount = 0
        toMatchI++
    }
    // println("test satisfiesFullContiguousBrokenCondition $inputLine $cumulativeBrokenCount $toMatchI ${toMatchI == brokenGroups.size}")
    return toMatchI == brokenGroups.size
}

private fun replaceCharAtIndex(input: String, index: Int, replaceChar: Char): String {
    val chars = input.toMutableList()
    chars[index] = replaceChar
    return chars.joinToString(separator = "")
}

// this will be recursive
private fun findPossibilities(inputLine: String, brokenGroups: List<Int>, index: Int): List<String> {
   // println("inputLine $inputLine index $index")
    if (index == inputLine.length) {
        if (satisfiesFullContiguousBrokenCondition(inputLine, brokenGroups)) {
            //println("satisfiesFullContiguousBrokenCondition $inputLine $brokenGroups")
            return listOf(inputLine)
        }
        // println("No satisfiesFullContiguousBrokenCondition $inputLine $brokenGroups")
        return listOf()
    }
    // for each ? char, sub # and .
    // and find possibilities with that
    return when(inputLine[index]) {
        '#' -> findPossibilities(inputLine, brokenGroups, index + 1)
        '.' -> findPossibilities(inputLine, brokenGroups, index + 1)
        '?' -> {
            val newStrings = listOf(
                replaceCharAtIndex(inputLine, index, '#'),
                replaceCharAtIndex(inputLine, index, '.')
            )
            newStrings.flatMap { findPossibilities(it, brokenGroups, index + 1) }
        }
        else -> {
            println("Does not match any condition")
            return listOf()
            }
        }
}

private fun parseInput(input: String): Pair<String, List<Int>> {
    val (gears, brokenGearsString) = input.split(' ')
    return Pair(gears, brokenGearsString.split(',').map(String::toInt))
}
private fun part1(inputLines: List<String>): Int {
    val parsed = inputLines.map { parseInput(it) }
    val possibilities = parsed.map{ findPossibilities(it.first, it.second, 0)}
    val numPossibilities = possibilities.map { it.distinct().size }
    return numPossibilities.sum()
}

fun main() {
    val testInput = readInputLines("Day12_test")
    println("testInput part1 ${part1(testInput)}")
}