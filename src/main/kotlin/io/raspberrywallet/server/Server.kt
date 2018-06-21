package io.raspberrywallet.server

import io.raspberrywallet.Manager
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch

class Server(private val manager: Manager) {
    fun start() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle(manager))
    }
}

internal class MainVerticle(private val manager: Manager) : CoroutineVerticle() {
    override suspend fun start() {
        val router = Router.router(vertx)
        router.get("/ping").coroutineHandler {
            it.response().end(json {
                obj ( "ping" to manager.ping()).encodePrettily()
            })
        }

        router.get("/modules").coroutineHandler {
            it.response().end(json {
                array (
                    manager.modules
                ).encodePrettily()

            })
        }
        router.get("/moduleState/:id").coroutineHandler {

            it.response().end(json {
                obj (
                    it.pathParam("id") to manager.getModuleState(it.pathParam("id"))
                ).encodePrettily()

            })
        }
        awaitResult<HttpServer> {
            vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config.getInteger("http.port", 9090), it)
        }
    }
}

/**
 * An extension method for simplifying coroutines usage with Vert.x Web routers
 */
fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
    handler { ctx ->
        launch(ctx.vertx().dispatcher()) {
            try {
                fn(ctx)
            } catch (e: Exception) {
                ctx.fail(e)
            }
        }
    }
}
