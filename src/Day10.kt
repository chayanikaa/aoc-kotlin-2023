fun main() {
    fun parseInput(input: List<String>): List<List<Long>> {
        return input.map { it.split(" ").map(String::toLong) }
    }

    fun part1(input: List<String>): Long {
        return 0L
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readInputLines("Day09_test")
     println("testInput part1 ${part1(testInput)}")
//     println("testInput part2 ${part2(testInput)}")
//
//     val input = readInputLines("Day09")
//      println("part1 ${part1(input)}")
//     println("part2 ${part2(input)}")
}
