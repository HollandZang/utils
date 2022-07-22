import file.FileUtil
import java.util.regex.Pattern

class FSM {
    val stateTransitionTableStr = FileUtil.readFile("conf", "flow_test")

    val stateTransitionTable = stateTransitionTableStr.run {
        val pattern = Pattern.compile("\t+")
        val map = mutableMapOf<String, MutableMap<String, String>>()
        val twoDimensionalArray = this.split("\r\n", "\n").map { it.split(pattern) }
        for (i in 1 until twoDimensionalArray[0].size) {
            val state = twoDimensionalArray[0][i]
            for (j in 1 until twoDimensionalArray.size) {
                val condition = twoDimensionalArray[j][0]
                val nextState = twoDimensionalArray[j][i]
                if (nextState != "-") {
                    map[state]?.set(condition, nextState)
                        ?: let { map[state] = mutableMapOf(condition to nextState) }
                }
            }
        }
        map
    }
}

class State(val value: String, val start: Boolean = false, val end: Boolean = false) {

}

fun main() {
    val fsm = FSM()
    println(fsm.stateTransitionTable)
}