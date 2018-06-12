package io.raspberrywallet.server

import io.raspberrywallet.BackendProvider
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class MainVerticle(val backend : BackendProvider?) : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        super.start(startFuture)
        vertx.createHttpServer().requestHandler({ req ->
            req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!")
        }).listen(8080)
        println("HTTP server started on port 8080")
    }

}
