package socket

import socket.compoment.CompomentScan
import socket.http.HttpRequest
import socket.http.HttpResponse
import socket.http.HttpRouter
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class ServerSocket

val newCachedThreadPool = ThreadPoolExecutor(
    10, Int.MAX_VALUE,
    60L, TimeUnit.SECONDS,
    SynchronousQueue()
)

fun main() {
    val ip = 7654
    val server = ServerSocket(ip)

    CompomentScan.execute()
    HttpRouter.init()
    HttpRouter.RouteMap.forEach { println("URI: ${it.key}\t${it.value.second}") }

    println("开启成功：$ip")
    while (true) {
        val clientSocket: Socket = server.accept()

        newCachedThreadPool.execute {
            val inputStream = clientSocket.getInputStream()
            Thread.sleep(20)
            val byteArray = ByteArray(inputStream.available())
            inputStream.read(byteArray, 0, byteArray.size)
            val requestStr = String(byteArray)
            println("收到请求: $requestStr")

            doOperate(clientSocket, requestStr)

            inputStream.close()
            clientSocket.close()
        }
    }
}

fun doOperate(clientSocket: Socket, requestStr: String) {
    // 当前默认识别成 http协议
    val request = HttpRequest(requestStr)
    val protocol = Protocol.checkProtocol(request)
    when (Protocol.checkProtocol(request)) {
        Protocol.`HTTP 1_1` -> {
            doHttpOperate(clientSocket, request)
        }
    }
}

private fun doHttpOperate(clientSocket: Socket, httpRequest: HttpRequest) {
    val result = HttpRouter invoke httpRequest
    doHttpResponse(clientSocket, result)
}

private fun doHttpResponse(clientSocket: Socket, result: Any) {
    val out = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
    val string = HttpResponse(result).toString()
    println("发送返回: $string")
    out.write(string)
    out.flush()
    out.close()
}
