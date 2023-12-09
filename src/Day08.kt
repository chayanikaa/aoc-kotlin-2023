data class NetworkNode(val left: String, val right: String)

fun main() {
    fun parseInput(input: String): Pair<String, Map<String, NetworkNode>> {
        val (instructions, nodesString) = input.split("\n\n").map(String::trim)
        val nodesMap = nodesString.split("\n").associate {
            val (source, targetsString) = it.split(" = ")
            val (left, right) = targetsString.substring(1, targetsString.length - 1).split(", ")
            source to NetworkNode(left.trim(), right)
        }
        return instructions to nodesMap
    }

    fun part1(input: String): Int {
        val (instructions, nodes) = parseInput(input)
        var current = "AAA"
        var foundZZZ = false
        var instructionsIndex = 0
        var steps = 0
        while (!foundZZZ) {
            val node = nodes[current]
            val instruction = instructions[instructionsIndex]
            if (node == null) {
                println("node not found $current")
                return -1
            }
            current = if (instructions[instructionsIndex] == 'R') {
                node.right
            } else {
                node.left
            }
            steps++
            if (current == "ZZZ") {
                foundZZZ = true
            }
            instructionsIndex = (instructionsIndex + 1) % instructions.length
        }
        return steps
    }


    fun part2(input: String): Long {
        val (instructions, nodes) = parseInput(input)
        val startingNodes = nodes.filter { it.key.endsWith('A') }

        val steps2ToZList = startingNodes.map {
            var instructionsIndex = 0
            var steps = 0L
            var currentNode = it.key
            while (!currentNode.endsWith('Z')) {
                val instruction = instructions[instructionsIndex]
                val node = nodes[currentNode]

                if (node == null) {
                    println("node not found $it")
                    return -1
                }

                currentNode = if (instructions[instructionsIndex] == 'R') {
                    node.right
                } else {
                    node.left
                }
                steps++
                instructionsIndex = (instructionsIndex + 1) % instructions.length
            }
            steps
        }
        return lcm(steps2ToZList)

    }

    val testInput = """
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()
    println("testInput part1 ${part1(testInput)}")

    val testInput2 = """
        LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent()
    println("testInput2 part1 ${part1(testInput2)}")

    val testInput3 = """
        LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
        """

    println("testInput3 part2 ${part2(testInput3)}")
    val input = readInput("Day08")
    println("part1 ${part1(input)}")
    println("part2 ${part2(input)}")
}
