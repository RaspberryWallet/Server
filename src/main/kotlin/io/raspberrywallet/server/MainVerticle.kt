package io.raspberrywallet.server

import io.raspberrywallet.BackendProvider
import io.raspberrywallet.ManagerMockup
import io.vertx.core.AbstractVerticle

class MainVerticle(private val backend: BackendProvider = ManagerMockup()) : AbstractVerticle() {

    override fun start() {
        vertx.createHttpServer().requestHandler({ req ->
            req.response()
                .putHeader("content-type", "text/plain")
                .end("Manager says ${backend.ping()}")
        }).listen(8080)
        println("HTTP server started on port 8080")
    }


}
