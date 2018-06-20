package io.raspberrywallet.server

import io.raspberrywallet.Manager
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class Server(private val manager: Manager) {
    fun start() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle(manager))
    }
}

internal class MainVerticle(private val manager: Manager) : CoroutineVerticle() {
    override suspend fun start() {
        vertx.createHttpServer().listen(9090)

        val router = Router.router(vertx)
        router.get("/ping").handler {
            manager.ping()
        }
        router.get("/modules").handler {
            manager.modules
        }
        router.get("/moduleState/:id").handler {
            manager.getModuleState(it.pathParam("id"))
        }
        router.post("/nextStep").handler {
            manager.nextStep(it.data()["moduleId"] as String, it.data()["input"] as ByteArray)
        }
    }
}
