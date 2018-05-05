package rs.emulate.scene3d

import rs.emulate.scene3d.backend.RenderTarget
import rs.emulate.scene3d.backend.Renderer
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock


class SceneAnimator(val scene: Scene, val renderer: Renderer, val renderTarget: RenderTarget) {

    /**
     * A lock held to notify or wait on the resume condition.
     */
    private val resumeLock = ReentrantLock()

    /**
     * A condition that is signaled to a paused renderer when it is to be resumed.
     */
    private val resumeCondition = resumeLock.newCondition()

    /**
     * A flag indicating if the renderer is paused.
     */
    private val paused = AtomicBoolean(false)

    /**
     * A flag indicating if the renderer is stopped.
     */
    private val stopped = AtomicBoolean(false)

    fun start() {
        val currentThread = Thread.currentThread()
        thread(start = true) {
            renderTarget.initialize()

            var last = System.currentTimeMillis()
            var updateAccumulator = 0L
            val updateInterval = 1000L / 30
            val renderInterval = 1000L / 60

            while (currentThread.isAlive && !stopped.get()) {
                if (paused.get()) {
                    resumeLock.withLock {
                        resumeCondition.await(PAUSE_TIMEOUT, TimeUnit.MILLISECONDS)
                    }
                } else {
                    val renderStart = System.currentTimeMillis()
                    val delta = renderStart - last

                    updateAccumulator += delta
                    while (updateAccumulator >= updateInterval) {
                        scene.update()
                        updateAccumulator -= updateInterval
                    }

                    if (scene.dirty) {
                        renderTarget.resize(scene.width, scene.height)
                        scene.dirty = false
                    }

                    renderer.render(scene, renderTarget, updateAccumulator / updateInterval / 1000f)
                    last = System.currentTimeMillis()

                    if (!renderTarget.isVsyncEnabled()) {
                        sync(last, renderInterval)
                    }
                }
            }
        }
    }

    /**
     * Pause the renderer thread until it is [resume]d or [stop]ped
     */
    fun pause() = paused.set(true)

    /**
     * Check if the renderer thread is currently paused.
     */
    fun paused() = paused.get()

    /**
     * Notify the renderer thread to resume rendering.
     */
    fun resume() {
        if (paused.getAndSet(false)) {
            resumeLock.withLock {
                resumeCondition.signal()
            }
        }
    }

    /**
     * Stop the renderer thread.
     */
    fun stop() = stopped.set(true)


    companion object {
        /**
         * The number of milliseconds to wait before resuming while paused.
         */
        const val PAUSE_TIMEOUT = 5000L

        /**
         * Synchronize to the given render interval based on the last render time.
         */
        private fun sync(lastRenderTime: Long, renderInterval: Long) {
            var now = System.currentTimeMillis()

            while (now - lastRenderTime < renderInterval) {
                Thread.yield()
                Thread.sleep(1)

                now = System.currentTimeMillis()
            }
        }

    }
}
