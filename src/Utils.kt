import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

data class Coordinate(val x: Int, val y: Int)

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = Path("inputs/$name.txt").readLines()

/**
 * Reads from the given input txt file.
 */
fun readInput(name: String) = Path("inputs/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun gcd(a: Long, b: Long): Long {
    if (b == 0L)
        return a;
    return gcd(b, a % b);
}

fun replaceCharAt(str: String, index: Int, replaceWith: Char): String {
    val chars = str.toMutableList()
    chars[index] = replaceWith
    return chars.joinToString(separator = "")
}

// Returns LCM of array elements
fun lcm(arr: List<Long>): Long {
    var ans = arr[0];

    for (num in arr)
        ans = (((num * ans)) /
                (gcd(num, ans)));

    return ans;
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
