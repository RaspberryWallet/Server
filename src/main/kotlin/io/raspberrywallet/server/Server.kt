package io.raspberrywallet.server

import io.raspberrywallet.Manager
import io.vertx.core.Vertx
import io.vertx.grpc.VertxServerBuilder

class Server(private val manager: Manager) {
    fun startHttpServer() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(HttpVerticle(manager)) {
            if (it.succeeded()) println("HTTP Server started")
            else {
                println("Could not startHttpServer HTTP server")
                it.cause().printStackTrace()
            }
        }
    }

    fun startGrpcServer() {
        val service = GrpcVerticle(manager)
        val vertxGRpc = Vertx.vertx()

        val grpcServer = VertxServerBuilder
            .forAddress(vertxGRpc, "localhost", 8081)
            .addService(service)
            .build()
            .start()


        println("gRPC Server started")
        Runtime.getRuntime().addShutdownHook(Thread { println("Ups, JVM shutdown") })
        grpcServer.awaitTermination()

        println("gRPC Server stopped")
    }
}
