import kotlin.math.absoluteValue

enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

private val pipeDirectionMap = mapOf(
    '|' to setOf(Direction.NORTH, Direction.SOUTH),
    '-' to setOf(Direction.EAST, Direction.WEST),
    'L' to setOf(Direction.NORTH, Direction.EAST),
    'J' to setOf(Direction.NORTH, Direction.WEST),
    '7' to setOf(Direction.SOUTH, Direction.WEST),
    'F' to setOf(Direction.SOUTH, Direction.EAST),
)

private val kneeBendPipes = setOf('L', 'J', '7', 'F')

private fun canConnectInDirection(direction: Direction, symbol: Char): Boolean {
    if (symbol == '.') return false
    val result = when (direction) {
        Direction.NORTH -> pipeDirectionMap[symbol]!!.contains(Direction.NORTH)
        Direction.SOUTH -> pipeDirectionMap[symbol]!!.contains(Direction.SOUTH)
        Direction.EAST -> pipeDirectionMap[symbol]!!.contains(Direction.EAST)
        Direction.WEST -> pipeDirectionMap[symbol]!!.contains(Direction.WEST)
    }
    return result
}
private fun findStart(input: List<String>): Pair<Int, Int> {
    var columnIndex: Int = -1
    val rowIndex = input.indexOfFirst { it ->
        columnIndex = it.indexOfFirst { it == 'S' }
        if (columnIndex != -1) true else false
    }
    return Pair(rowIndex, columnIndex)
}

private fun findStartType(startCoords: Pair<Int, Int>, map: List<String>): Char {
    // note: this part was a waste of time, this can be done much more easily manually
    val (rowIndex, columnIndex) = startCoords
    val directions: MutableSet<Direction> = mutableSetOf()
    if (rowIndex > 0 && canConnectInDirection(Direction.SOUTH, map[rowIndex-1][columnIndex])) {
        directions.add(Direction.NORTH)
    }
    if (rowIndex < map.size-1 && canConnectInDirection(Direction.NORTH, map[rowIndex+1][columnIndex])) {
        directions.add(Direction.SOUTH)
    }
    if (columnIndex > 0 && canConnectInDirection(Direction.EAST, map[rowIndex][columnIndex-1])) {
        directions.add(Direction.WEST)
    }
    if (columnIndex < map[0].length-1 && canConnectInDirection(Direction.WEST,map[rowIndex][columnIndex+1])) {
        directions.add(Direction.EAST)
    }
    return when (directions) {
        setOf(Direction.NORTH, Direction.SOUTH) -> '|'
        setOf(Direction.EAST, Direction.WEST) -> '-'
        setOf(Direction.NORTH, Direction.EAST) -> 'L'
        setOf(Direction.NORTH, Direction.WEST) -> 'J'
        setOf(Direction.SOUTH, Direction.WEST) -> '7'
        setOf(Direction.SOUTH, Direction.EAST) -> 'F'
        else -> throw Exception("Invalid start type")
    }
}

private fun getNextSteps(coords: Pair<Int, Int>, symbol: Char): List<Pair<Int, Int>> {
    val (rowIndex, columnIndex) = coords
    return when (symbol) {
        '|' -> listOf(Pair(rowIndex+1, columnIndex), Pair(rowIndex-1, columnIndex))
        '-' -> listOf(Pair(rowIndex, columnIndex+1), Pair(rowIndex, columnIndex-1))
        'L' -> listOf(Pair(rowIndex-1, columnIndex), Pair(rowIndex, columnIndex+1))
        'J' -> listOf(Pair(rowIndex-1, columnIndex), Pair(rowIndex, columnIndex-1))
        '7' -> listOf(Pair(rowIndex+1, columnIndex), Pair(rowIndex, columnIndex-1))
        'F' -> listOf(Pair(rowIndex+1, columnIndex), Pair(rowIndex, columnIndex+1))
        else -> throw Exception("Invalid symbol")
    }
}

private fun travelAlongPipe(start: Pair<Int, Int>, map: List<String>): List<Pair<Int, Int>> {
    val (rowIndex, columnIndex) = start
    val startType = map[rowIndex][columnIndex]
    val directions = pipeDirectionMap[startType]!!
    val visited: MutableList<Pair<Int, Int>> = mutableListOf()
    var currentCoords: Pair<Int,Int>? = start
    var i = 0
    while (currentCoords != null) {
        visited.add(currentCoords)
        val (currentRowIndex, currentColumnIndex) = currentCoords
        val currentType = map[currentRowIndex][currentColumnIndex]
        val currentDirections = pipeDirectionMap[currentType]!!
        val nextPossibleSteps = getNextSteps(currentCoords, currentType)
        // println("currentCoords $currentCoords, currentType $currentType, currentDirections $currentDirections, nextStep $nextPossibleSteps")
        currentCoords = nextPossibleSteps.find() { !visited.contains(it) }
        i++
    }
    return visited
}

private fun isMatchingVerticalBorder(coords: Pair<Int, Int>, map: List<String>, loopCoords: List<Pair<Int, Int>>): Boolean {
    val (rowIndex, columnIndex) = coords
    val index = loopCoords.indexOf(coords)
    if (index == -1) return false
    val symbol = map[rowIndex][columnIndex]
    // I've tried the parity solution, also including the other knee bend pipes, but it doesn't work
    // I would think, that checking actual parity for example F with 7 would work, but it doesn't
    // OMG it works because you only consider either the upper or lower not both
    // So, both of the below work
    // this works because if
//         return symbol in listOf('|', 'L', 'J')
    return symbol in listOf('|', 'F', '7')
}

private fun findEnclosedTiles(updatedMap: List<String>, loopCoords: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
    var enclosedTiles: MutableList<Pair<Int, Int>> = mutableListOf()
    for (rowIndex in updatedMap.indices) {
        var closed = true
        for (columnIndex in 0 until updatedMap[0].length) {
            val coords = Pair(rowIndex, columnIndex)
            if (isMatchingVerticalBorder(coords, updatedMap, loopCoords)) {
                closed = !closed
            }
            if (!closed && !loopCoords.contains(coords)) {
                enclosedTiles.add(coords)
            }
        }
    }
    return enclosedTiles
}

fun printPath(map: List<String>, path: List<Pair<Int, Int>>, enclosedTiles: List<Pair<Int, Int>>) {
    for (rowIndex in map.indices) {
        for (columnIndex in 0 until map[0].length) {
            val coords = Pair(rowIndex, columnIndex)
            val pathIndex = path.indexOf(coords)
            if (pathIndex != -1) {
//                    print(pathIndex)
                print(map[rowIndex][columnIndex])
            } else if (enclosedTiles.contains(coords)) {
                print("*")
            } else {
                print(".")
            }
        }
        println()
    }
}

private fun part1(input: List<String>): Long {
    val start = findStart(input)
    val startType = findStartType(start, input)
    val updatedMap = input.map { it.replace('S', startType) }
    val loopCoords = travelAlongPipe(start, updatedMap)
//        println(loopCoords)
    return loopCoords.size / 2L
}

private fun part2(input: List<String>): Int {
    val start = findStart(input)
    val startType = findStartType(start, input)
    val updatedMap = input.map { it.replace('S', startType) }
    val loopCoords = travelAlongPipe(start, updatedMap)
    val enclosedTiles = findEnclosedTiles(updatedMap, loopCoords)
    printPath(input, loopCoords, enclosedTiles)
    return enclosedTiles.size

}

fun main() {
    val testInput = readInputLines("Day10_test2")
     println("testInput part1 ${part1(testInput)}")
     println("testInput part2 ${part2(testInput)}")

     val input = readInputLines("Day10")
      println("part1 ${part1(input)}")
     println("part2 ${part2(input)}")
}
