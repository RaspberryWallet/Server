package io.raspberrywallet.server

import io.raspberrywallet.BackendProvider
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx

class Server(private val backend: BackendProvider){
    fun start(){
        val vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle(backend))
    }
}

class MainVerticle(private val backend: BackendProvider) : AbstractVerticle() {
    override fun start() {
        vertx.createHttpServer().requestHandler({ req ->
            req.response()
                .putHeader("content-type", "text/plain")
                .end("Manager says ${backend.ping()}")
        }).listen(8080)
        println("HTTP server started on port 8080")
    }


}
