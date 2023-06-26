class ToJava {
}

fun main() {
    K::class.java.declaredFields.forEach {
        println("private ${it.type.simpleName} ${it.name};")
    }
}

data class K(
    var id: String,
    var msgTitle: String,
    var sendTime: String,
    var READ_FLAG: Int,
    var cjsj: String,
    var nr: String,
    var msgCategory: String,
    var msgContent: String,
    var openPage: String,
    var openPage1: String,
    var openPage2: String,
)