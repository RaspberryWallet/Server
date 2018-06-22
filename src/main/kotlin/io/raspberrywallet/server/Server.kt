package io.raspberrywallet.server

import com.stasbar.Logger
import io.raspberrywallet.Manager
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch
import java.util.*


class Server(private val manager: Manager) {
    fun start() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(MainVerticle(manager)) {
            if (it.succeeded()) println("Server started")
            else {
                println("Could not start server")
                it.cause().printStackTrace()
            }
        }
    }
}

internal class MainVerticle(private val manager: Manager) : CoroutineVerticle() {
    override suspend fun start() {
        val router = Router.router(vertx)
        acceptCORS(router)
        router.get("/ping").coroutineHandler {
            Logger.d("/ping")
            it.response().end(json {
                obj("ping" to manager.ping()).encodePrettily()
            })
        }
        router.get("/modules").coroutineHandler {
            Logger.d("/modules")
            it.response().end(json {
                array(
                    manager.modules
                ).encodePrettily()

            })
        }
        router.get("/moduleState/:id").coroutineHandler {


            it.response().end(json {
                Logger.d("/moduleState/${it.pathParam("id")}")
                val moduleState = manager.getModuleState(it.pathParam("id"))
                obj(
                    "state" to moduleState.name, "message" to moduleState.message
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

fun acceptCORS(router: Router) {
    val allowedHeaders = HashSet<String>()
    allowedHeaders.add("x-requested-with")
    allowedHeaders.add("Access-Control-Allow-Origin")
    allowedHeaders.add("origin")
    allowedHeaders.add("Content-Type")
    allowedHeaders.add("accept")
    allowedHeaders.add("X-PINGARUNER")

    val allowedMethods = HashSet<HttpMethod>()
    allowedMethods.add(HttpMethod.GET)
    allowedMethods.add(HttpMethod.POST)
    allowedMethods.add(HttpMethod.OPTIONS)

    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethod(HttpMethod.GET))
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
