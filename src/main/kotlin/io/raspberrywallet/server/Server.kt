package io.raspberrywallet.server

import io.raspberrywallet.BackendProvider
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle

class Server(private val manager: BackendProvider) {
    fun start() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle(manager))
    }
}

class MainVerticle(private val manager: BackendProvider) : CoroutineVerticle() {
    override suspend fun start() {
        vertx.createHttpServer().requestHandler({ req ->
            req.response()
                .putHeader("content-type", "application/json")
                .end(manager.ping())

        }).listen(9090)

        val router = Router.router(vertx)
        router.get("/ping").handler { manager.ping() }
    }
}
