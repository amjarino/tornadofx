package tornadofx.tests

import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.junit.BeforeClass
import org.junit.Test
import org.testfx.api.FxToolkit
import tornadofx.*
import kotlin.test.assertEquals

abstract class BaseInterceptor:ChildInterceptor{
    var intercepted: Boolean = false
}
class FirstInterceptor : BaseInterceptor() {
    override fun invoke(parent: EventTarget, node: Node, index: Int?): Boolean = when (parent) {
        is Pane -> {
            intercepted = true
            true
        }
        else -> false
    }
}

class SecondInterceptor : BaseInterceptor() {
    override fun invoke(parent: EventTarget, node: Node, index: Int?): Boolean = when (parent) {
        is Pane -> {
            intercepted = true
            true
        }
        else -> false
    }
}

class MyTestView : View("TestView") {
    override val root = pane {
        button {}
    }
}

class ChildInterceptorTest {


    companion object {

        @JvmStatic
        @BeforeClass
        fun before() {
            val primaryStage: Stage = FxToolkit.registerPrimaryStage()
            val app = App(MyTestView::class)
            FX.registerApplication(FxToolkit.setupApplication {
                app
            }, primaryStage)
        }
    }

    @Test
    fun interceptorsLoaded() {
        assertEquals(FX.childInterceptors.size, 2)
    }


    @Test
    fun onlyOneInterceptorShouldWork() {
        assertEquals(FX.childInterceptors.map { it as BaseInterceptor }.filter { it.intercepted }.size,1)
    }
}