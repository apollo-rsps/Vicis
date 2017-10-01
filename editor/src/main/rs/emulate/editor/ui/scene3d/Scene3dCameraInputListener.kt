package rs.emulate.editor.ui.scene3d


import com.jme3.app.Application
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import java.util.*

//@todo - big hack from JME3-JFX docs
class Scene3dCameraInputListener {
    internal class KeyActions(var onPressed: Runnable, var onReleased: Runnable)

    var jme: Application? = null
    var speed = 1.0f

    private fun driver(): Scene3dCameraState? {
        return if (jme == null) null else jme!!.stateManager.getState(Scene3dCameraState::class.java)
    }

    fun upPressed() {
        val d = driver()
        if (d != null) d.up = 1.0f * speed
    }

    fun upReleased() {
        val d = driver()
        if (d != null) d.up = 0.0f * speed
    }

    fun downPressed() {
        val d = driver()
        if (d != null) d.down = 1.0f * speed
    }

    fun downReleased() {
        val d = driver()
        if (d != null) d.down = 0.0f * speed
    }

    fun rightPressed() {
        val d = driver()
        if (d != null) d.right = 1.0f * speed
    }

    fun rightReleased() {
        val d = driver()
        if (d != null) d.right = 0.0f * speed
    }

    fun leftPressed() {
        val d = driver()
        if (d != null) d.left = 1.0f * speed
    }

    fun leftReleased() {
        val d = driver()
        if (d != null) d.left = 0.0f * speed
    }

    fun forwardPressed() {
        val d = driver()
        if (d != null) d.forward = 1.0f * speed
    }

    fun forwardReleased() {
        val d = driver()
        if (d != null) d.forward = 0.0f * speed
    }

    fun backwardPressed() {
        val d = driver()
        if (d != null) d.backward = 1.0f * speed
    }

    fun backwardReleased() {
        val d = driver()
        if (d != null) d.backward = 0.0f * speed
    }

    companion object {
        fun bindDefaults(c: ImageView, driver: Scene3dCameraInputListener) {
            c.isFocusTraversable = true
            c.hoverProperty().addListener { ob, o, n -> if (n!!) c.requestFocus() }
            c.addEventFilter(MouseEvent.MOUSE_CLICKED) { e -> c.requestFocus() }

            val inputMap = HashMap<KeyCode, KeyActions>()
            inputMap.put(KeyCode.PAGE_UP, KeyActions(Runnable { driver.upPressed() }, Runnable { driver.upReleased() }))
            inputMap.put(
                KeyCode.PAGE_DOWN,
                KeyActions(Runnable { driver.downPressed() }, Runnable { driver.downReleased() })
            )

            // arrow
            inputMap.put(
                KeyCode.UP,
                KeyActions(Runnable { driver.forwardPressed() }, Runnable { driver.forwardReleased() })
            )
            inputMap.put(
                KeyCode.LEFT,
                KeyActions(Runnable { driver.leftPressed() }, Runnable { driver.leftReleased() })
            )
            inputMap.put(
                KeyCode.DOWN,
                KeyActions(Runnable { driver.backwardPressed() }, Runnable { driver.backwardReleased() })
            )
            inputMap.put(
                KeyCode.RIGHT,
                KeyActions(Runnable { driver.rightPressed() }, Runnable { driver.rightReleased() })
            )

            //WASD
            inputMap.put(
                KeyCode.W,
                KeyActions(Runnable { driver.forwardPressed() }, Runnable { driver.forwardReleased() })
            )
            inputMap.put(KeyCode.A, KeyActions(Runnable { driver.leftPressed() }, Runnable { driver.leftReleased() }))
            inputMap.put(
                KeyCode.S,
                KeyActions(Runnable { driver.backwardPressed() }, Runnable { driver.backwardReleased() })
            )
            inputMap.put(KeyCode.D, KeyActions(Runnable { driver.rightPressed() }, Runnable { driver.rightReleased() }))

            // ZQSD
            inputMap.put(
                KeyCode.Z,
                KeyActions(Runnable { driver.forwardPressed() }, Runnable { driver.forwardReleased() })
            )
            inputMap.put(KeyCode.Q, KeyActions(Runnable { driver.leftPressed() }, Runnable { driver.leftReleased() }))
            c.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
                val ka = inputMap[event.code]
                ka?.onPressed?.run()
                event.consume()
            }
            c.addEventHandler(KeyEvent.KEY_RELEASED) { event ->
                val ka = inputMap[event.code]
                ka?.onReleased?.run()
                event.consume()
            }
        }
    }

}