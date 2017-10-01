package rs.emulate.editor.ui.scene3d

import com.jme3.app.Application
import com.jme3.app.state.AbstractAppState
import com.jme3.app.state.AppStateManager
import com.jme3.math.Vector3f


class Scene3dCameraState : AbstractAppState() {
    internal lateinit var app: Application

    var up = 0f
    var down = 0f
    var forward = 0f
    var backward = 0f
    var left = 0f
    var right = 0f
    //to continue yaw pitch ...

    //tmp store
    private val v3 = Vector3f()

    override fun initialize(stateManager: AppStateManager, app: Application) {
        super.initialize(stateManager, app)
        this.app = app
    }

    override fun update(tpf: Float) {
        super.update(tpf)
        val cam = app.camera
        val pos = cam.location

        cam.getLeft(v3)
        v3.multLocal((left - right) * tpf)
        pos.addLocal(v3)

        cam.getDirection(v3)
        v3.multLocal((forward - backward) * tpf)
        pos.addLocal(v3)

        cam.getUp(v3)
        v3.multLocal((up - down) * tpf)
        pos.addLocal(v3)

        cam.location = pos
    }
}