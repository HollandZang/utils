import java.io.File
import java.io.FileWriter

@Suppress("unused")
class Trie(
    private val char: Char? = null
) {
    private var data: Set<String>? = null
    private var children: Array<Trie>? = null

    infix fun insert(word: String) {
        val chars = word.toLowerCase().toCharArray()
        var currNode = this
        val endIdx = word.length - 1
        for (i in word.indices) {
            val c = chars[i]
            val child = currNode.children?.find { it.char == c }
            if (child != null) {
                currNode = child
            } else {
                Trie(c).let {
                    currNode.children = currNode.children?.plus(it) ?: arrayOf(it)
                    currNode = it
                }
            }
            if (i == endIdx) {
                currNode.data = currNode.data?.plus(word) ?: setOf(word)
            }
        }
    }

    fun match(prefix: String, limit: Int = 19): Set<String> {
        val chars = prefix.toLowerCase().toCharArray()
        var currNode = this
        for (i in prefix.indices) {
            val c = chars[i]
            val child = currNode.children?.find { it.char == c }
            if (child != null) {
                currNode = child
            } else {
                return setOf()
            }
        }

        val container = mutableSetOf<String>()
        try {
            recursion(currNode, container, limit)
        } catch (ignore: InterruptedException) {
        }
        return container
    }

    fun sort() {
        sort(this)
    }

    fun dump(path: String = ".", fileName: String = "Trie.dump") {
        val fileWriter = FileWriter("$path${File.separatorChar}$fileName")
        dump(this, -1, "ROOT", fileWriter)
        fileWriter.close()
    }

    companion object {
        private fun recursion(trie: Trie, container: MutableSet<String>, limit: Int) {
            trie.data?.let { container.addAll(it) }
            if (limit > 0 && container.size > limit) throw InterruptedException()
            trie.children?.forEach { recursion(it, container, limit) }
        }

        private fun sort(trie: Trie) {
            trie.children?.sortBy { it.char }
            trie.children?.forEach { sort(it) }
        }

        private fun dump(trie: Trie, int: Int, char: String? = trie.char?.toString(), fileWriter: FileWriter) {
            var space = ""
            for (i in 0..int) space += " "
            trie.data
                ?.let { fileWriter.write("$space|$char :${it.joinToString(", ")}\n") }
                ?: fileWriter.write("$space|$char\n")
            trie.children?.forEach { dump(it, int + 1, fileWriter = fileWriter) }
        }
    }
}