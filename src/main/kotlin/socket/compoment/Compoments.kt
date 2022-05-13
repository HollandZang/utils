package socket.compoment

import socket.http.annotation.RequestMapping

@Suppress("SpellCheckingInspection")
object Compoments {
    /**
     * k: className
     * v: proxyClass
     */
    val proxyClass = mutableMapOf<String, Any>()

    /**
     * k: class
     * v: annotation
     */
    val httpRequestController = mutableMapOf<Class<*>, RequestMapping>()
}
