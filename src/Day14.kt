private fun findNewPositionForRoundRockNorth(coord: Coordinate, grid: List<String>): Coordinate {
    if (coord.y == 0) {
        return coord
    }
    for(row in (coord.y -1) downTo 0) {
        val currentChar = grid[row][coord.x]
        if (listOf('#', 'O').contains(currentChar)) {
            return Coordinate(coord.x, row+1)
        }
    }
    return Coordinate(coord.x, 0)
}

private fun findNewPositionForRoundRockSouth(coord: Coordinate, grid: List<String>): Coordinate {
    if (coord.y == grid.size - 1) {
        return coord
    }
    for(row in (coord.y + 1) ..<grid.size) {
        val currentChar = grid[row][coord.x]
        if (listOf('#', 'O').contains(currentChar)) {
            return Coordinate(coord.x, row-1)
        }
    }
    return Coordinate(coord.x, grid.size - 1)
}

private fun findNewPositionForRoundRockWest(coord: Coordinate, grid: List<String>): Coordinate {
    if (coord.x == 0) {
        return coord
    }
    for(col in (coord.x - 1) downTo 0) {
        val currentChar = grid[coord.y][col]
        if (listOf('#', 'O').contains(currentChar)) {
            return Coordinate(col + 1, coord.y)
        }
    }
    return Coordinate(0, coord.y)
}

private fun findNewPositionForRoundRockEast(coord: Coordinate, grid: List<String>): Coordinate {
    if (coord.x == (grid.size - 1)) {
        return coord
    }
    for(col in (coord.x + 1) ..<grid.size) {
        val currentChar = grid[coord.y][col]
        if (listOf('#', 'O').contains(currentChar)) {
            return Coordinate(col - 1, coord.y)
        }
    }
    return Coordinate(grid.size - 1, coord.y)
}

private fun calculateWeight(grid: List<String>): Long {
    var sum = 0L
    for (row in grid.indices) {
        for (col in grid[0].indices) {
            if (grid[row][col] == 'O') {
                sum += (grid.size - row)
            }
        }
    }
    return sum
}

private fun moveRocks(input: List<String>, findNewPositionFunction: (coord: Coordinate, grid: List<String>) -> Coordinate): List<String> {
    val grid = input.toMutableList()

    do {
        var numSwitched = 0
        for (row in grid.indices) {
            for (col in grid[0].indices) {
                if (grid[row][col] == 'O') {
                    val oldCoords = Coordinate(col, row)
                    val newCoords = findNewPositionFunction(Coordinate(col, row), grid)
                    if (newCoords != oldCoords) {
                        numSwitched++
                        grid[newCoords.y] = replaceCharAt(grid[newCoords.y], newCoords.x, 'O')
                        grid[row] = replaceCharAt(grid[row], col, '.')
                    }
                }
            }
        }
    } while (numSwitched != 0)

    return grid.toList()
}
private fun part1(input: List<String>): Long {
    val tiltedGrid = moveRocks(input, ::findNewPositionForRoundRockNorth)
    return calculateWeight(tiltedGrid)
}

private fun part2(input: List<String>, nCycles: Long): Long {
    val previousStatesMap: MutableMap<String, Long> = mutableMapOf()
    val moveFunctions = listOf(
        ::findNewPositionForRoundRockNorth,
        ::findNewPositionForRoundRockWest,
        ::findNewPositionForRoundRockSouth,
        ::findNewPositionForRoundRockEast
    )
    var tiltedGrid = input
    for (i in 1..nCycles) {
        moveFunctions.forEach {
            tiltedGrid = moveRocks(tiltedGrid, it)
        }
        val cacheKey = tiltedGrid.joinToString(" ")
        if (previousStatesMap[cacheKey] != null) {
            val firstOccurance = previousStatesMap[cacheKey]!!
            val cycleLength = i - firstOccurance
            return part2(tiltedGrid, (nCycles - i) % cycleLength)
        }
        previousStatesMap[tiltedGrid.joinToString(" ")] = i
    }
    return calculateWeight(tiltedGrid)
}

fun main() {
    val testInput = readInputLines("Day14_test")
    println("testInput part1 ${part1(testInput)}")
    println("testInput part2 ${part2(testInput, 1000000000)}")

    val input = readInputLines("Day14")
    println("part1 ${part1(input)}")
    println("part2 ${part2(input, 1000000000)}")
}
