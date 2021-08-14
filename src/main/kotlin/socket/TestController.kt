package socket

import socket.http.annotation.RequestMapping
import java.util.*

@RequestMapping
class TestController {

    @RequestMapping
    fun index(): String {
        return "测试根路径：${Date().toString()}"
    }

    @RequestMapping("/t1")
    fun index_1(): Any {
        return a("测试t1路径：${Date().toString()}")
    }
}

class a(val string: String)
