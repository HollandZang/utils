package socket

import java.net.Socket
import java.util.*
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class SocketClient

fun main(args: Array<String>) {
    val newCachedThreadPool = ThreadPoolExecutor(
        0, Int.MAX_VALUE,
        60L, TimeUnit.SECONDS,
        SynchronousQueue()
    )

    for (i in 0..9) {
        newCachedThreadPool.execute {
            val socket = Socket("localhost", 7654)
            socket.getOutputStream().flush()
            socket.getOutputStream().write("Hello Socket ${Date()}, $i".toByteArray())

            socket.close()
        }
    }

    Thread.sleep(100)
    exitProcess(0)
}

