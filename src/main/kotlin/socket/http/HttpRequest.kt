package socket.http

import com.google.gson.Gson
import socket.Request

class HttpRequest(string: String) : Request {
    val requestLine: RequestLine
    val requestHead: Map<String, String>
    val requestBody: RequestBody?

    init {
        val split = string.split("\n")
        val requestLine = split[0]
        this.requestLine = RequestLine(requestLine)

        requestHead = mutableMapOf()
        var content = ""
        for (index in 1 until split.size) {
            if (split[index].trim().isEmpty()) {
                content = split.subList(index + 1, split.size).joinToString("\n")
                break
            }
            val kv = split[index].split(":")
            requestHead[kv[0]] = kv[1].trim()
        }
        requestBody = RequestBody(content)
    }

    /**
     * Method SP Request-URI SP HTTP-Version CRLF
     * example: GET http://www.w3.org/pub/WWW/TheProject.html HTTP/1.1
     *
     * Note that the absolute path cannot be empty; if none is present in the original URI, it MUST be given as "/" (the server root).
     */
    class RequestLine(requestLine: String) {
        val method: String
        var requestURI: String
        val httpVersion: String
        val paramiters: HashMap<String, String>

        init {
            val split = requestLine.split(" ")
            this.method = split[0]
            this.requestURI = split[1].ifEmpty { "/" }
            this.httpVersion = split[2].trim()

            paramiters = hashMapOf()
            val split1 = requestURI.split("?")
            if (split1.size > 1) {
                this.requestURI = split1[0]
                split1[1].split("&").forEach { params ->
                    run {
                        val list = params.split("=")
                        paramiters[list[0]] = list[1]
                    }
                }
            }
        }

        override fun toString(): String =
            "$method $requestURI $httpVersion"

    }

    class RequestBody(val content: String) {
        fun <T> getClass(clazz: Class<T>): T? {
            return Gson().fromJson(content, clazz)
        }

        override fun toString(): String {
            return if (content.isEmpty()) "" else "\n\n$content"
        }

    }

    override fun toString(): String =
        "$requestLine\n${requestHead.keys.joinToString("\n") { "$it: ${requestHead[it]}" }}$requestBody"

}
