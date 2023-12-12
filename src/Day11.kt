import kotlin.math.absoluteValue

private data class Day11GalaxyMap(val map: List<String>, val emptyRows: List<Int>, val emptyCols: List<Int>)
private fun parseInput(input: List<String>): Day11GalaxyMap {
    val emptyRows = mutableListOf<Int>()
    val emptyCols = mutableListOf<Int>()
    input.forEachIndexed() { index, line ->
        if (line.all { it == '.' }) {
            emptyRows.add(index)
        }
    }
    for (i in input[0].indices) {
        val column = input.map { it[i] }
        if (column.all { it == '.' }) {
            emptyCols.add(i)
        }
    }
    return Day11GalaxyMap(input, emptyRows, emptyCols)
}

private fun getCoordinates(galaxyMap: Day11GalaxyMap, expansionFactor: Int): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var rowIndex = 0
    galaxyMap.map.forEachIndexed { unprocessedRowIndex, row ->
        if (galaxyMap.emptyRows.contains(unprocessedRowIndex)) {
            rowIndex+=expansionFactor
        } else {
            var colIndex = 0
            row.forEachIndexed { unprocessedColIndex, char ->
                if (galaxyMap.emptyCols.contains(unprocessedColIndex)) {
                    colIndex+=expansionFactor
                } else if (char == '#') {
                    coordinates.add(Coordinate(colIndex, rowIndex))
                    colIndex++
                } else {
                    colIndex++
                }
            }
            rowIndex++
        }
    }
    return coordinates.toList()
}

private fun getAllUniquePairs(coordinates: List<Coordinate>): List<Pair<Coordinate, Coordinate>> {
    val pairs = mutableListOf<Pair<Coordinate, Coordinate>>()
    coordinates.forEachIndexed { index, coordinate ->
        for (i in index + 1 until coordinates.size) {
            pairs.add(coordinate to coordinates[i])
        }
    }
    return pairs.toList()
}

private fun shortestDistance(a: Coordinate, b: Coordinate): Long {
    return (a.x - b.x).absoluteValue.toLong() + (a.y - b.y).absoluteValue.toLong()
}

private fun solve(input: List<String>, expansionFactor: Int): Long {
    val galaxyMap = parseInput(input)
    val coordinates = getCoordinates(galaxyMap, expansionFactor)
    val pairs = getAllUniquePairs(coordinates)
    val shortestDistances = pairs.map { shortestDistance(it.first, it.second) }

    return shortestDistances.sum()
}

fun main() {
    val testInput = readInputLines("Day11_test")
    println("testInput part1 ${solve(testInput, 2)}")
    println("testInput part2 ${solve(testInput, 100)}")

    val input = readInputLines("Day11")
    println("part1 ${solve(input, 2)}")
    println("part2 ${solve(input, 1000000)}")
}
