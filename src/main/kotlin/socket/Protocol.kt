package socket

import socket.http.HttpRequest

enum class Protocol(val value: String) {
    `HTTP 1`(""),
    `HTTP 1_1`("HTTP/1.1"),
    WEB_SOCKET("");

    companion object {
        fun checkProtocol(httpRequest: HttpRequest): Protocol {
            if (isHttp1_1(httpRequest)) {
                return if (isWebSocket(httpRequest)) WEB_SOCKET else `HTTP 1_1`
            }
            throw IllegalArgumentException("Protocol is not found")
        }

        private fun isWebSocket(httpRequest: HttpRequest): Boolean {
            val upgrade = httpRequest.requestHead["Upgrade"]
            return (upgrade.isNullOrBlank() || "websocket" != upgrade.toLowerCase()).not()
        }

        private fun isHttp1_1(httpRequest: HttpRequest): Boolean {
            return httpRequest.requestLine.httpVersion == "HTTP/1.1"
        }
    }
}
