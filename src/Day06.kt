private data class Race(val duration: Long, val record: Long) {
    fun calculateWinningRangeSize(): LongRange {
        var i: Long = 0
        val startWinningRange = (0..duration).find { it -> it * (duration - it) > record }
        val endWinningRange: Long? = (duration downTo 0).find { it -> it * (duration - it) > record }
        if (startWinningRange == null || endWinningRange == null) {
            return 0L..0L
        }
        return startWinningRange..endWinningRange
    }
}

fun main() {
    fun parseInput(input: String): List<Race> {
        val (timeStrings, distanceStrings) = input.split("\n")
        val times = timeStrings.split(Regex("[\\s+]")).drop(1).filter { it.isNotBlank() }.map(String::toLong)
        val distances = distanceStrings.split(Regex("[\\s+]")).filter { it.isNotBlank() }.drop(1).map(String::toLong)
        return times.zip(distances).map { Race(it.first, it.second) }
    }

    fun parseInput2(input: String): Race {
        val (timeString, distanceString) = input.split("\n")
        val time = timeString.split(Regex("[\\s+]")).drop(1).filter { it.isNotBlank() }.joinToString("").toLong()
        val distance =
            distanceString.split(Regex("[\\s+]")).filter { it.isNotBlank() }.drop(1).joinToString("").toLong()
        return Race(time, distance)
    }

    fun part1(input: String): Long {
        val races = parseInput(input)
        val winningRanges = races.map { it.calculateWinningRangeSize() }
        val winningRangeSizes = winningRanges.map { it.last - it.first + 1 }

        winningRanges.println()
        return winningRangeSizes.fold(1) { acc, i -> acc * i }
    }

    fun part2(input: String): Long {
        val range = parseInput2(input).calculateWinningRangeSize()
        return range.last - range.first + 1
    }

    val testInput = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()
    println("testInput part 1 ${part1(testInput)}")
    println("testInput part 2 ${part2(testInput)}")

    val input = """
         Time:        47     84     74     67
         Distance:   207   1394   1209   1014
     """.trimIndent()
    println("part1 ${part1(input)}")
    println("part2 ${part2(input)}")
}
