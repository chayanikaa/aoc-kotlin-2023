enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

val pipeDirectionMap = mapOf(
    '|' to setOf(Direction.NORTH, Direction.SOUTH),
    '-' to setOf(Direction.EAST, Direction.WEST),
    'L' to setOf(Direction.NORTH, Direction.EAST),
    'J' to setOf(Direction.NORTH, Direction.WEST),
    '7' to setOf(Direction.SOUTH, Direction.WEST),
    'F' to setOf(Direction.SOUTH, Direction.EAST),
)

val kneeBendPipes = setOf('L', 'J', '7', 'F')

fun canConnectInDirection(direction: Direction, symbol: Char): Boolean {
    if (symbol == '.') return false
    val result = when (direction) {
        Direction.NORTH -> pipeDirectionMap[symbol]!!.contains(Direction.NORTH)
        Direction.SOUTH -> pipeDirectionMap[symbol]!!.contains(Direction.SOUTH)
        Direction.EAST -> pipeDirectionMap[symbol]!!.contains(Direction.EAST)
        Direction.WEST -> pipeDirectionMap[symbol]!!.contains(Direction.WEST)
    }
    println("canConnectInDirection $direction, $symbol, $result")
    return result
}

fun main() {
    fun findStart(input: List<String>): Pair<Int, Int> {
        var columnIndex: Int = -1
        val rowIndex = input.indexOfFirst { it ->
            columnIndex = it.indexOfFirst { it == 'S' }
            if (columnIndex != -1) true else false
        }
        return Pair(rowIndex, columnIndex)
    }

    fun findStartType(startCoords: Pair<Int, Int>, map: List<String>): Char {
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

    fun getNextSteps(coords: Pair<Int, Int>, symbol: Char): List<Pair<Int, Int>> {
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

//    fun getNextStep(coords: Pair<Int, Int>, symbol: Char): Pair<Int, Int> {
//        val (rowIndex, columnIndex) = coords
//        return when (symbol) {
//            '|' -> Pair(rowIndex+1, columnIndex)
//            '-' -> Pair(rowIndex, columnIndex+1)
//            'L' -> Pair(rowIndex-1, columnIndex-1)
//            'J' -> Pair(rowIndex-1, columnIndex+1)
//            '7' -> Pair(rowIndex+1, columnIndex-1)
//            'F' -> Pair(rowIndex+1, columnIndex+1)
//            else -> throw Exception("Invalid symbol")
//        }
//    }

    fun travelAlongPipe(start: Pair<Int, Int>, map: List<String>): List<Pair<Int, Int>> {
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

    fun isVerticalBorder(coords: Pair<Int, Int>, map: List<String>, loopCoords: List<Pair<Int, Int>>): Boolean {
        val (rowIndex, columnIndex) = coords
        if (!loopCoords.contains(coords)) return false
        if (rowIndex != 0 && loopCoords.contains(Pair(rowIndex-1, columnIndex))) return true
        if (rowIndex != map.size-1 && loopCoords.contains(Pair(rowIndex+1, columnIndex))) return true
        return false
    }

    fun findEnclosedTiles(updatedMap: List<String>, loopCoords: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        var enclosedTiles: MutableList<Pair<Int, Int>> = mutableListOf()
        println("loopCoords $loopCoords")
        for (rowIndex in updatedMap.indices) {
            var closed = true
            for (columnIndex in 0 until updatedMap[0].length) {

                val coords = Pair(rowIndex, columnIndex)
                var symbol = updatedMap[rowIndex][columnIndex]
                println("coords $coords, symbol $symbol, closed $closed")
                if (!closed && symbol == '.') {
                    enclosedTiles.add(coords)
                }
                if (isVerticalBorder(coords, updatedMap, loopCoords)) {
                    closed = !closed
                }
            }
        }
        println("enclosedTiles $enclosedTiles")
        return enclosedTiles
    }

    fun part1(input: List<String>): Long {
        val start = findStart(input)
        val startType = findStartType(start, input)
        val updatedMap = input.map { it.replace('S', startType) }
        val loopCoords = travelAlongPipe(start, updatedMap)
//        println(loopCoords)
        return loopCoords.size / 2L
    }

    fun part2(input: List<String>): Int {
        val start = findStart(input)
        val startType = findStartType(start, input)
        val updatedMap = input.map { it.replace('S', startType) }
        val loopCoords = travelAlongPipe(start, updatedMap)
        val enclosedTiles = findEnclosedTiles(updatedMap, loopCoords)
        return enclosedTiles.size
    }

    val testInput = readInputLines("Day10_test2")
    testInput.println()
//     println("testInput part1 ${part1(testInput)}")
     println("testInput part2 ${part2(testInput)}")
//
     val input = readInputLines("Day10")
//      println("part1 ${part1(input)}")
//     println("part2 ${part2(input)}")
}
