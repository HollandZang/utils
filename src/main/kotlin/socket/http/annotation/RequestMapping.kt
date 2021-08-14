package socket.http.annotation

import socket.compoment.Compoment
import socket.http.enums.HttpRequestMethod
import socket.http.enums.HttpRequestType

@Compoment
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestMapping(
    val path: String = "/",
    val method: HttpRequestMethod = HttpRequestMethod.GET,
    val type: HttpRequestType = HttpRequestType.FORM,
)
