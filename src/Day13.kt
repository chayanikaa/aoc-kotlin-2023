import kotlin.text.StringBuilder

private fun isRowMirrored(grid: List<String>, row1Index: Int, row2Index: Int): Int = grid[row1Index].diff(grid[row2Index])

private fun isColumnMirrored(grid: List<String>, col1Index: Int, col2Index: Int): Int {
    var string1 = StringBuilder()
    val string2 = StringBuilder()
    grid.forEach {
        string1.append(it[col1Index])
        string2.append(it[col2Index])
    }
    return string1.toString().diff(string2.toString())
}

fun String.diff (string2: String): Int {
    var diff = 0
    for (i in indices) {
        if (this[i] != string2[i]) {
            diff++
        }
    }
    return diff
}

private fun checkCompleteMirrorRows(grid: List<String>, startIndex: Int, endIndex: Int, isRow: Boolean, diff: Int = 0): Boolean {
    var cumulativeDiff = 0
    for (index in 0..(endIndex - startIndex) / 2) {
        val currentIndex1 = startIndex + index
        val currentIndex2 = endIndex - index
        cumulativeDiff += if (isRow) {
            isRowMirrored(grid, currentIndex1, currentIndex2)
        } else {
            isColumnMirrored(grid, currentIndex1, currentIndex2)
        }
    }
    return cumulativeDiff == diff
}

data class MirrorCheckResult(val mirrorMidline: Int, val mirrorSize: Int)

private fun findMirroredRowLine(grid: List<String>, desiredDiff: Int = 0): MirrorCheckResult {
    var maxMidLine = 0
    var maxMirrorSize = 0
    val lastIndex = grid.size - 1
    for (rowIndex in 0..<lastIndex) {
//        println("rowIndex $rowIndex lastIndex $lastIndex desiredDiff $desiredDiff ${checkCompleteMirrorRows(grid, rowIndex, lastIndex, true, desiredDiff)}")
        if ((lastIndex - rowIndex) % 2 == 0) continue
        val mirror = checkCompleteMirrorRows(grid, rowIndex, lastIndex, true, desiredDiff)
        if (mirror) {
            maxMirrorSize = (lastIndex - rowIndex + 1).coerceAtLeast(maxMirrorSize)
            maxMidLine = rowIndex + (lastIndex - rowIndex + 1) / 2
            return MirrorCheckResult(maxMidLine, maxMirrorSize)
        }
    }
    for (rowIndex in lastIndex downTo 1) {
        if (rowIndex % 2 == 0) continue
        val mirror = checkCompleteMirrorRows(grid, 0, rowIndex, true, desiredDiff)
        if (mirror) {
            maxMirrorSize = (rowIndex + 1).coerceAtLeast(maxMirrorSize)
            maxMidLine = (rowIndex + 1) / 2
            return MirrorCheckResult(maxMidLine, maxMirrorSize)
        }
    }
    return MirrorCheckResult(maxMidLine, maxMirrorSize)
}

private fun findMirroredColumnLine(grid: List<String>, desiredDiff: Int = 0): MirrorCheckResult {
    var maxMirrorSize = 0
    var maxMidLine = 0
    val lastIndex = grid[0].length - 1
    for (colIndex in 0..<lastIndex) {
        if ((lastIndex - colIndex) % 2 == 0) continue
        val mirror = checkCompleteMirrorRows(grid, colIndex, lastIndex, false, desiredDiff)
        if (mirror) {
            maxMirrorSize = (lastIndex - colIndex + 1)
            maxMidLine = colIndex + (lastIndex - colIndex + 1) / 2
            return MirrorCheckResult(maxMidLine, maxMirrorSize)
        }
    }
    for (colIndex in lastIndex downTo 1) {
        if (colIndex % 2 == 0) continue
        val mirror = checkCompleteMirrorRows(grid, 0, colIndex, false, desiredDiff)
        if (mirror) {
            maxMirrorSize = (colIndex + 1).coerceAtLeast(maxMirrorSize)
            maxMidLine = (colIndex + 1) / 2
            return MirrorCheckResult(maxMidLine, maxMirrorSize)
        }
    }
    return MirrorCheckResult(maxMidLine, maxMirrorSize)
}

private fun parseInput(input: String): List<List<String>> {
    val inputStrings = input.split("\n\n")
    return inputStrings.map { it.split('\n') }
}

private fun solve(input: String, desiredDiff: Int = 0): Int {
    val parsed = parseInput(input)
    val mirroredLineResults = parsed.sumOf {
        val rowResult = findMirroredRowLine(it, desiredDiff)
        val colResult = findMirroredColumnLine(it, desiredDiff)
        if (colResult.mirrorSize > rowResult.mirrorSize) {
            colResult.mirrorMidline
        } else {
            rowResult.mirrorMidline * 100
        }
    }
    // findMirroredRow and Column, take the most
    return mirroredLineResults
}

fun main() {
    val testInput = readInput("Day13_test")
    println("testInput part1 ${solve(testInput)}")
    println("testInput part2 ${solve(testInput, 1)}")
//
    val input = readInput("Day13")
    println("part1 ${solve(input)}")
    println("part2 ${solve(input, 1)}")
}

// part 2 too low 12212

