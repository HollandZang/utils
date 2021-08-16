package socket.http

import socket.compoment.ZnCompoments
import socket.http.annotation.RequestMapping
import java.lang.reflect.Method


object HttpRouter {
    /**
     * k: path
     * v: class, method
     */
    val RouteMap = mutableMapOf<String, Pair<Any, Method>>()

    fun init() {
        ZnCompoments.httpRequestController.forEach { (clazz, requestMapping) ->
            clazz.methods.forEach { method ->
                if (method.annotations.isEmpty()) return
                method.annotations.forEach {
                    if (it.annotationClass.qualifiedName == RequestMapping::class.java.name) {
                        val classPath =
                            if (requestMapping.path == "/") "" else if (requestMapping.path.startsWith("/")) requestMapping.path else "/$requestMapping.path"
                        val methodPath1 = (it as RequestMapping).path
                        val methodPath = if (methodPath1.startsWith("/")) methodPath1 else "/$methodPath1"
                        RouteMap["$classPath$methodPath"] = ZnCompoments.proxyClass[clazz.name]!! to method
                    }
                }
            }
        }
    }

    fun invoke(httpRequest: HttpRequest): Any {
        val pair = RouteMap[httpRequest.requestLine.requestURI.split("?")[0]]
            ?: // TODO: 2021/8/14 404 Page
            return "404 Page"
        // TODO: 2021/8/14 此处可配置拦截器
        // TODO: 2021/8/14 获取参数列表，调带参实现
        return pair.second.invoke(pair.first)
    }
}
