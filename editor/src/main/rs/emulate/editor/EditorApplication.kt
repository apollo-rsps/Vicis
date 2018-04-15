package rs.emulate.editor

import javafx.application.Application
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import tornadofx.*
import kotlin.reflect.KClass

class EditorApplication : App(EditorView::class) {
    init {
        val springContext = AnnotationConfigApplicationContext("rs.emulate.editor")

        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T = springContext.getBean(type.java)
            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = springContext.getBean(name, type.java)
        }

        importStylesheet(EditorStyles::class)
    }
}

fun main(args: Array<String>) {
    Application.launch(EditorApplication::class.java, *args)
}