import javax.naming.PartialResultException

private fun satisfiesPartialContiguousBrokenCondition(inputLine: String, brokenGroups: List<Int>): Boolean {
    var toMatchI = 0
    var cumulativeBrokenCount = 0
    for (index in inputLine.indices) {
        val char = inputLine[index]
        when (char) {
            '#' -> {
                cumulativeBrokenCount++
                if (toMatchI < brokenGroups.size && cumulativeBrokenCount > brokenGroups[toMatchI]) {
                    return false
                }
            }

            '.' -> {
                if (toMatchI < brokenGroups.size && cumulativeBrokenCount > 0 && cumulativeBrokenCount != brokenGroups[toMatchI]) {
                    return false
                } else if (toMatchI < brokenGroups.size && cumulativeBrokenCount == brokenGroups[toMatchI]) {
                    toMatchI++
                }
                cumulativeBrokenCount = 0
            }
        }
    }
    return true
}

private fun getMemoCheckPartialMatchFunction(brokenGroups: List<Int>): (String) -> Boolean {
    val resultMap = mapOf<String, Boolean>()
    return fun(inputLine: String): Boolean {
        // println("resultMap[inputLine] ${resultMap[inputLine]}")
        return if (resultMap[inputLine] != null) {
            resultMap[inputLine]!!
        } else satisfiesPartialContiguousBrokenCondition(inputLine, brokenGroups)
    }
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
private fun findPossibilities(
    inputLine: String,
    brokenGroups: List<Int>,
    index: Int,
    cacheMap: MutableMap<String, Int> = mutableMapOf(),
    partialResultCheckFunction: (String) -> Boolean = getMemoCheckPartialMatchFunction(
        brokenGroups
    ),
): Int {
    // println("inputLine $inputLine index $index")
    if (cacheMap[inputLine] != null) {
        return cacheMap[inputLine]!!
    }
    if (index == inputLine.length) {
        if (satisfiesFullContiguousBrokenCondition(inputLine, brokenGroups)) {
            // println("satisfiesFullContiguousBrokenCondition $inputLine $brokenGroups")
            return 1
        }
        // println("No satisfiesFullContiguousBrokenCondition $inputLine $brokenGroups")
        return 0
    }
    // for each ? char, sub # and .
    // and find possibilities with that
    val result = when (inputLine[index]) {
        '?' -> {
            val newStrings = listOf(
                replaceCharAtIndex(inputLine, index, '#'),
                replaceCharAtIndex(inputLine, index, '.')
            )
            newStrings.sumOf { findPossibilities(it, brokenGroups, index + 1, cacheMap, partialResultCheckFunction) }
        }

        else -> {
            if (partialResultCheckFunction(inputLine.substring(0, index + 1))) {
                findPossibilities(inputLine, brokenGroups, index + 1, cacheMap, partialResultCheckFunction)
            } else 0
        }
    }
    if (cacheMap[inputLine] == null) {
        cacheMap[inputLine] = result
    }
    return result
}

private fun parseInput(input: String): Pair<String, List<Int>> {
    val (gears, brokenGearsString) = input.split(' ')
    return Pair(gears, brokenGearsString.split(',').map(String::toInt))
}

private fun part1(inputLines: List<String>): Int {
    val parsed = inputLines.map { parseInput(it) }
    val possibilities = parsed.map { findPossibilities(it.first, it.second, 0) }
    return possibilities.sum()
}

private fun part2(inputLines: List<String>): Int {
    val processedInput = inputLines.map {
        val (gearsString, brokenGroupsString) = it.split(' ')
        val gearStringRepeated = MutableList(5) { gearsString }.joinToString("?")
        val brokenGroupsStringRepeated = MutableList(5) { brokenGroupsString }.joinToString(",")
        "$gearStringRepeated $brokenGroupsStringRepeated"
    }
    // return 0L

    val parsed = processedInput.map { parseInput(it) }
    val possibilities = parsed.map { it ->
        val result = findPossibilities(it.first, it.second, 0)
        println("processed ${it.first} $result")
        result
    }
    return possibilities.sum()
}

fun main() {
    val testInput = readInputLines("Day12_test")
//     println("testInput part1 ${part1(testInput)}")
    println("testInput part2 ${part2(testInput)}")
}