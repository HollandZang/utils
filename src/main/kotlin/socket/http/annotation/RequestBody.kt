package socket.http.annotation

import socket.compoment.Compoment

@Compoment
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestBody(
)
