package socket

import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class ServerSocket

val newCachedThreadPool = ThreadPoolExecutor(
    0, Int.MAX_VALUE,
    60L, TimeUnit.SECONDS,
    SynchronousQueue()
)

fun main() {
    val ip = 8080
    val server = ServerSocket(ip)

    println("开启成功：$ip")
    while (true) {
        val clientSocket: Socket = server.accept()

        newCachedThreadPool.execute {
            println("收到请求")

            val inputStream = clientSocket.getInputStream()
            val requestStr: String = String(inputStream.readNBytes(inputStream.available()))

            val request = HttpRequest(requestStr)
            println(requestStr)

            // TODO: 2021/8/12 当前默认识别成 http协议
//            val protocol = Protocol.checkProtocol(request)
            doHttpResponse(clientSocket)

            inputStream.close()
            clientSocket.close()
            println("关闭会话\n")
        }
    }
}

fun doHttpResponse(clientSocket: Socket) {
    val out = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
    val string = HttpResponse().toString()
    println("发送返回")
    println(string)
    out.write(string)
    out.flush()
    out.close()
}
