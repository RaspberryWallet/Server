package io.raspberrywallet.server

import io.raspberrywallet.Manager
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult

class Server(private val manager: Manager) {
    fun start() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle(manager))
    }
}

internal class MainVerticle(private val manager: Manager) : CoroutineVerticle() {
    override suspend fun start() {
        val router = Router.router(vertx)
        router.get("/ping").blockingHandler {
            println("/ping")
            manager.ping()
        }
        router.get("/modules").blockingHandler {
            println("/modules")
            manager.modules
        }
        router.get("/moduleState/:id").blockingHandler {
            println("/moduleState/:id")
            manager.getModuleState(it.pathParam("id"))
        }
        awaitResult<HttpServer> { vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(config.getInteger("http.port", 9090), it)
        }
    }
}
