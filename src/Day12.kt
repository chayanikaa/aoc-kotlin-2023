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
        return if (resultMap[inputLine] != null) {
            resultMap[inputLine]!!
        } else satisfiesPartialContiguousBrokenCondition(inputLine, brokenGroups)
    }
}

private fun satisfiesFullContiguousBrokenCondition(inputLine: String, brokenGroups: List<Int>): Boolean {
    var toMatchI = 0
    var cumulativeBrokenCount = 0
    for (index in 0..1) {
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
    return toMatchI == brokenGroups.size
}

private fun replaceCharAtIndex(input: String, index: Int, replaceChar: Char): String {
    val chars = input.toMutableList()
    chars[index] = replaceChar
    return chars.joinToString(separator = "")
}

data class CacheKey(val inputLine: String, val brokenGroupsToFind: List<Int>, val cumulativeBrokenCount: Int)

// this will be recursive
private fun findPossibilities(
    inputLine: String,
    brokenGroups: List<Int>,
    cacheMap: MutableMap<CacheKey, Long> = mutableMapOf(),
    partialResultCheckFunction: (String) -> Boolean = getMemoCheckPartialMatchFunction(
        brokenGroups
    ),
    trailingCumulativeCount: Int = 0
): Long {
    if (brokenGroups.isEmpty() && !inputLine.contains('#') && trailingCumulativeCount == 0) {
        return 1
    } else if (brokenGroups.isEmpty()) {
        return 0
    }
    if (cacheMap[CacheKey(inputLine, brokenGroups, trailingCumulativeCount)] != null) {
        return cacheMap[CacheKey(inputLine, brokenGroups, trailingCumulativeCount)]!!
    }
    var cumulativeBrokenCount = trailingCumulativeCount
    var updatedBrokenGroups = brokenGroups
    for (index in inputLine.indices) {
        if (updatedBrokenGroups.isEmpty()) {
            return findPossibilities(inputLine.substring(index), updatedBrokenGroups, cacheMap, partialResultCheckFunction, cumulativeBrokenCount)
        }
        when (inputLine[index]) {
            '.' -> {
                if (cumulativeBrokenCount == updatedBrokenGroups[0]) {
                    updatedBrokenGroups = updatedBrokenGroups.drop(1)

                } else if (cumulativeBrokenCount != 0 && cumulativeBrokenCount < updatedBrokenGroups[0]) {
                    return 0
                }
                cumulativeBrokenCount = 0
            }
            '#' -> {
                cumulativeBrokenCount += 1
                if (cumulativeBrokenCount > updatedBrokenGroups[0]) {
                    return 0
                }
            }
            '?' -> {
                val newStrings = listOf(
                    "#" + inputLine.substring(index+1),
                    "." + inputLine.substring(index+1)
                )
                return newStrings.sumOf {
                    val result = findPossibilities(it, updatedBrokenGroups, cacheMap, partialResultCheckFunction, cumulativeBrokenCount)
                    cacheMap[CacheKey(it, updatedBrokenGroups, cumulativeBrokenCount)] = result
                    result
                }
            }
        }
    }
    if (updatedBrokenGroups.isNotEmpty() && cumulativeBrokenCount == updatedBrokenGroups[0]) {
        updatedBrokenGroups = updatedBrokenGroups.drop(1)
    }
    if (updatedBrokenGroups.isEmpty()) {
        return 1
    }
    return 0
}

private fun parseInput(input: String): Pair<String, List<Int>> {
    val (gears, brokenGearsString) = input.split(' ')
    return Pair(gears, brokenGearsString.split(',').map(String::toInt))
}

private fun part1(inputLines: List<String>): Long {
    val parsed = inputLines.map { parseInput(it) }
    val possibilities = parsed.map { findPossibilities(it.first, it.second ) }
//    possibilities.forEach(::println)
    return possibilities.sum()
}

private fun part2(inputLines: List<String>): Long {
    val processedInput = inputLines.map {
        val (gearsString, brokenGroupsString) = it.split(' ')
        val gearStringRepeated = MutableList(5) { gearsString }.joinToString("?")
        val brokenGroupsStringRepeated = MutableList(5) { brokenGroupsString }.joinToString(",")
        "$gearStringRepeated $brokenGroupsStringRepeated"
    }
    // return 0L

    val parsed = processedInput.map { parseInput(it) }
    val possibilities = parsed.map { it ->
        val result = findPossibilities(it.first, it.second)
        // println("processed ${it.first} $result")
        result
    }
    return possibilities.sum()
}

fun main() {
    val testInput = readInputLines("Day12_test")
    println("testInput part1 ${part1(testInput)}")
    println("testInput part2 ${part2(testInput)}")

    val input = readInputLines("Day12")
    println("testInput part1 ${part1(input)}")
    println("testInput part2 ${part2(input)}")
}