package socket.compoment

import socket.http.annotation.RequestMapping
import java.io.File

@Suppress("SpellCheckingInspection")
object CompomentScan {
    private val AllClass = mutableSetOf<String>()
    private var currPackage = ""

    fun execute() {
        findAllClass("")
    }

    private fun findAllClass(path: String) {
        if (path.endsWith(".class")) {
            val classFullName = "$currPackage.${path.substring(0, path.indexOf(".class"))}"
            AllClass += classFullName
            initCompoment(classFullName)
        }

        val resources = Thread.currentThread().contextClassLoader.getResources(path.replace('.', '/'))

        while (resources.hasMoreElements()) {
            val element = resources.nextElement()
            when (element.protocol) {
                "file" -> {
                    if (path == "META-INF") break
//                    println("file $path")
                    File(element.path).list()?.forEach {
                        currPackage = path
                        findAllClass(it)
                    }
                }
                "jar" -> {
//                    println("jar  $path")
                }
                else -> {
                    println("else $path")
                }
            }
        }
    }

    /**
     * 初始化组件
     * 在此增强类本类
     */
    private fun initCompoment(classFullName: String) {
        val clazz = Class.forName(classFullName)
        if (clazz.annotations.isEmpty()) return
        val annoSet = mutableSetOf<Annotation>()
        clazz.annotations.forEach {
            recursionAnnotatnions(it, annoSet)
        }
        annoSet.forEach {
            when (it.annotationClass.qualifiedName) {
                Compoment::class.java.name -> {
                    ZnCompoments.proxyClass[classFullName] = clazz.getDeclaredConstructor().newInstance()
                }
                RequestMapping::class.java.name -> {
                    ZnCompoments.httpRequestController[clazz] = it as RequestMapping
                }
                // TODO: 2021/8/14 此处可配置AOP
                else -> {
                }
            }
        }
    }

    private fun recursionAnnotatnions(annotation: Annotation, annoSet: Set<Annotation>) {
        if (
            annotation.annotationClass.qualifiedName == "kotlin.Metadata" ||
            annotation.annotationClass.qualifiedName == "kotlin.annotation.Target" ||
            annotation.annotationClass.qualifiedName == "kotlin.annotation.Retention" ||
            annotation.annotationClass.qualifiedName == "kotlin.annotation.MustBeDocumented" ||
            annotation.annotationClass.qualifiedName == "java.lang.annotation.Documented" ||
            annotation.annotationClass.qualifiedName == "java.lang.annotation.Target" ||
            annotation.annotationClass.qualifiedName == "java.lang.annotation.Retention"
        ) {
            return
        }

        (annoSet as HashSet).add(annotation)
        annotation.annotationClass::java.get().annotations.forEach {
            annoSet.add(annotation)
            if (it.annotationClass::java.get().annotations.isNotEmpty()) {
                recursionAnnotatnions(it, annoSet)
            }
        }
    }
}
