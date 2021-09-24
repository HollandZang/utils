package socket.http

import socket.compoment.ZnCompoments
import socket.http.annotation.RequestBody
import socket.http.annotation.RequestMapping
import java.lang.reflect.Method
import java.lang.reflect.Parameter


object HttpRouter {
    /**
     * k: path
     * v: class, method
     */
    val RouteMap = mutableMapOf<String, Pair<Any, Method>>()

    fun init() {
        ZnCompoments.httpRequestController.forEach { (clazz, requestMapping) ->
            clazz.methods.forEach { method ->
                if (method.annotations.isNotEmpty()) {
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
    }

    fun invoke(httpRequest: HttpRequest): Any {
        val pair = RouteMap[httpRequest.requestLine.requestURI]
        if (pair == null) {
            // TODO: 2021/8/14 404 Page
        }
        // TODO: 2021/8/14 此处可配置拦截器
        val toList = pair!!.second.parameters.map { it1 ->
            val requestBody = it1.getAnnotation(RequestBody::class.java)
            if (requestBody != null) {
                httpRequest.requestBody?.content
            } else {
                val arg = httpRequest.requestLine.paramiters[it1.name]

                // todo 需要从string转换成对应的类型，目前没什么好办法
                typeConverter(arg, it1)
            }
        }.toList()

        return pair.second.invoke(pair.first, *toList.toTypedArray())
    }

    // todo string为空但是是基本类型，就会报错。不想写了，先这样
    private fun typeConverter(arg: String?, parameter: Parameter): Any? {
        if (arg == null) return null
        return when (parameter.parameterizedType.typeName) {
            "java.lang.String" -> arg
            "int" -> Integer.valueOf(arg) ?: 0
            "java.lang.Integer" -> Integer.valueOf(arg)
            else -> arg
        }
    }
}
