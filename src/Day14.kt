private fun findNewPositionForRoundRock(coord: Coordinate, grid: List<String>): Coordinate {
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

private fun moveRocksNorth(input: List<String>): List<String> {
    val grid = input.toMutableList()

    do {
        var numSwitched = 0
        for (row in 1..<grid.size) {
            for (col in grid[0].indices) {
                if (grid[row][col] == 'O') {
                    val oldCoords = Coordinate(col, row)
                    val newCoords = findNewPositionForRoundRock(Coordinate(col, row), grid)
                    if (newCoords != oldCoords) {
                        numSwitched++
//                        println("old coords $oldCoords")
//                        println("new coords $newCoords")
                        grid[newCoords.y] = replaceCharAt(grid[newCoords.y], newCoords.x, 'O')
                        grid[row] = replaceCharAt(grid[row], col, '.')
//                        grid.forEach(::println)
//                        println("-----------------------")
                    }


                }
            }
        }
    } while (numSwitched != 0)

    return grid.toList()
}
private fun part1(input: List<String>): Long {
    val tiltedGrid = moveRocksNorth(input)
    return calculateWeight(tiltedGrid)
}

fun main() {
    val testInput = readInputLines("Day14_test")
    println("testInput part1 ${part1(testInput)}")

    val input = readInputLines("Day14")
    println("part1 ${part1(input)}")
}
