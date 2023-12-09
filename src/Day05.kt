import org.w3c.dom.ls.LSInput

data class RangePair(val sourceRange: LongRange, val destinationRange: LongRange)
data class SewingMap(val name: String, val ranges: List<RangePair>) {
    fun findDestinationRange(inputValue: Long): RangePair? = ranges.find { it.sourceRange.contains(inputValue) }
    // if no destination range is found, source is same as destination
}


data class Seeds (val seeds: List<Long>)

data class SeedRanges (val ranges: List<LongRange>)

fun parseSeeds(seedInput: String): Seeds = Seeds(seedInput.split(" ").drop(1).map(String::toLong))

fun parseSeedRanges(seedInput: String): SeedRanges {
    val inputParts = seedInput.split(" ").drop(1)
    // break them up in pairs
    // rangeUntil is exclusive
    val ranges = inputParts.chunked(2).map { it[0].toLong().rangeUntil(it[0].toLong() + it[1].toLong()) }
    return SeedRanges(ranges)
}

fun parseSewingMap(input: String): SewingMap {
    val inputParts = input.split("\n")
    val name = inputParts[0].split(":")[0]
    val rangesStrings = inputParts.drop(1)
    val sewingMapRangePair = rangesStrings.map { it.split(" ").map(String::toLong) }
        .map { it ->
            val sourceStart = it[1]
            val destinationStart = it[0]
            val rangeLength = it[2]
            RangePair(sourceStart.rangeUntil(sourceStart + rangeLength), destinationStart.rangeUntil(destinationStart + rangeLength))
        }
    return SewingMap(name, sewingMapRangePair)
}

fun main() {
    fun parseInput(input: String): Pair<Seeds, List<SewingMap>> {
        val inputParts = input.split("\n\n")
        val mapInputs = inputParts.drop(1)
        return Pair(parseSeeds(inputParts[0]), mapInputs.map { parseSewingMap(it) })
    }

    fun parseInput2(input: String): Pair<SeedRanges, List<SewingMap>> {
        val inputParts = input.split("\n\n")
        val mapInputs = inputParts.drop(1)
        return Pair(parseSeedRanges(inputParts[0]), mapInputs.map { parseSewingMap(it) })
    }
    fun part1(input: String): Long {
        val (seeds, sewingMaps) = parseInput(input)

        val locations = seeds.seeds.map { it ->
            var destinationMapped = it
            for (sewingMap in sewingMaps) {
                val rangePair = sewingMap.findDestinationRange(destinationMapped)
                if (rangePair != null) {
                    destinationMapped = rangePair.destinationRange.first + (destinationMapped - rangePair.sourceRange.first)
                }
            }
            destinationMapped
        }

        return locations.min()
    }

    fun part2(input: String): Long {
        // there is possibly a better solution than this, since this is essentially brute forcing it
        // TODO: find a better solution
        val (seedRanges, sewingMaps) = parseInput2(input)
        var minLocation = Long.MAX_VALUE

        for (range in seedRanges.ranges) {
            for (seed in range) {
                var destinationMapped = seed
                for (sewingMap in sewingMaps) {
                    val rangePair = sewingMap.findDestinationRange(destinationMapped)
                    if (rangePair != null) {
                        destinationMapped = rangePair.destinationRange.first + (destinationMapped - rangePair.sourceRange.first)
                    }
                }
                if (destinationMapped < minLocation) {
                    minLocation = destinationMapped
                }
            }
            println("range processed $range")
        }
        return minLocation
    }

    val testInput = readInput("Day05_test")
    println("testInput ${part1(testInput)}")
    println("testInput ${part2(testInput)}")

     val input = readInput("Day05")
     part1(input).println()
    part2(input).println()
}
