package socket.http

import com.google.gson.Gson
import socket.Protocol
import socket.Response


class HttpResponse(result: Any) : Response {
    val statusLine: StatusLine
    val responseHead: Map<String, String>
    val responseBody: String

    init {
        statusLine = StatusLine()
        val body = Gson().toJson(result)
        responseHead = mutableMapOf(
            "Content-Type" to "application/json;UTF-8",
            "Content-Encoding" to "UTF-8",
            "Content-Length" to body.toByteArray().size.toString()
        )
        responseBody = body
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
        "$statusLine\r\n${responseHead.keys.joinToString("\r\n") { "$it: ${responseHead[it]}" }}\r\n\r\n$responseBody"
}
