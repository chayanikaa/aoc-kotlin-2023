private fun isRowMirrored(grid: List<String>, row1Index: Int, row2Index:Int) = grid[row1Index] == grid[row2Index]

private fun isColumnMirrored(grid: List<String>, col1Index: Int, col2Index:Int): Boolean {
    return grid.all{
        it[col1Index] == it[col2Index]
    }
}

private fun checkCompleteMirrorRows(grid: List<String>, startIndex: Int, endIndex: Int, isRow: Boolean): Boolean {
    for (index in 0..(endIndex - startIndex) / 2) {
        val currentIndex1 = startIndex + index
        val currentIndex2 = endIndex - index
        if (isRow) {
            if (!isRowMirrored(grid, currentIndex1, currentIndex2)) return false
        } else {
            if (!isColumnMirrored(grid, currentIndex1, currentIndex2)) return false
        }
    }
    return true
}

data class MirrorCheckResult(val mirrorMidline: Int, val mirrorSize: Int)
private fun findMirroredRowLine(grid: List<String>): MirrorCheckResult {
    var mirroredRowIndex = 0
    var maxMidLine = 0
    var maxMirrorSize = 0
    var maxPossibleMirrorSize = grid.size / 2
    var mirrorSize = 0
    for (rowIndex in grid.indices) {
        for (row2Index in (grid.size - 1) downTo (rowIndex + 1)) {
            if ((row2Index - rowIndex) % 2 == 0) {
                continue
            }
            val mirror = checkCompleteMirrorRows(grid, rowIndex, row2Index, true)
            if (mirror) {
                if ((row2Index - rowIndex + 1) > maxMirrorSize) {
                    maxMirrorSize = (row2Index - rowIndex + 1).coerceAtLeast(maxMirrorSize)
                    maxMidLine = rowIndex + ( row2Index - rowIndex + 1) / 2
                }
            }
        }
        maxPossibleMirrorSize--
    }
    return MirrorCheckResult(maxMidLine,maxMirrorSize)
}

private fun findMirroredColumnLine(grid: List<String>): MirrorCheckResult {
    var mirroredRowIndex = 0
    var maxMirrorSize = 0
    var maxMidLine = 0
    var maxPossibleMirrorSize = grid[0].length / 2
    var mirrorSize = 0
    val rowLength = grid[0].length
    for (colIndex in 0..rowLength) {
        for (col2Index in (rowLength - 1) downTo (colIndex + 1)) {
            if ((col2Index - colIndex) % 2 == 0) {
                continue
            }
            val mirror = checkCompleteMirrorRows(grid, colIndex, col2Index, false)
            if (mirror) {
                if ((col2Index - colIndex + 1) > maxMirrorSize) {
                    maxMirrorSize = (col2Index - colIndex + 1).coerceAtLeast(maxMirrorSize)
                    maxMidLine = colIndex + ( col2Index - colIndex + 1) / 2
                }
                // println("$colIndex, $col2Index, $maxMirrorSize")
                // return MirrorCheckResult(midLine, maxMirrorSize)
            }
           // println("$colIndex $col2Index $mirror")
        }
        maxPossibleMirrorSize--
    }
    return MirrorCheckResult(maxMidLine,maxMirrorSize)
}
private fun parseInput(input: String): List<List<String>> {
    val inputStrings = input.split("\n\n")
    return inputStrings.map { it.split('\n') }
}

private fun part1(input: String): Int {
    val parsed = parseInput(input)
    val mirroredLineResults = parsed.sumOf {
        val colResult = findMirroredColumnLine(it)
        val rowResult = findMirroredRowLine(it)
        it.forEach(::println)
        println("colResult $colResult")
        println("rowResult $rowResult")
        if (colResult.mirrorSize > rowResult.mirrorSize) {
            colResult.mirrorMidline
        } else {
            rowResult.mirrorMidline * 100
        }
    }
    // findMirroredRow and Column, take the most
    return mirroredLineResults
}

private fun part2(input: String): Long {
    return 0L
}

fun main() {
//    val testInput = readInput("Day13_test")
//    println("testInput part1 ${part1(testInput)}")
//    println("testInput part2 ${part2(testInput)}")
//
    val input = readInput("Day13")
    println("part1 ${part1(input)}")
//    println("testInput part2 ${part2(input)}")
}
