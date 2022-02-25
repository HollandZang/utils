/**
 * @author  zhn
 * @date  2022/1/10 14:52
 */
class ExpansionPattern {
}

fun String.isOk(): Boolean {
    return this.trim() == "OK"
}

fun main() {
    println("NOT OK".isOk())
    println("  OK  ".isOk())
}