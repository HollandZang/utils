package socket


class HttpResponse : Response {
    val statusLine: StatusLine
    val responseHead: Map<String, String>
    val responseBody: String

    init {
        statusLine = StatusLine()
        responseHead = mapOf("Content-Type" to "application/json", "Content-Encoding" to "UTF-8")
        responseBody = ""
    }

    /**
     * HTTP-Version SP Status-Code SP Reason-Phrase CRLF
     */
    class StatusLine {
        val httpVersion = Protocol.`HTTP 1_1`
        val statusCode = 200
        val reasonPhrase = "OK"

        override fun toString(): String =
            "${httpVersion.value} $statusCode $reasonPhrase"
    }

    override fun toString(): String =
        "$statusLine\n${responseHead.keys.joinToString("\n") { "$it: ${responseHead[it]}" }}$responseBody"
}