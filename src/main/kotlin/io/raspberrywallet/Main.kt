package io.raspberrywallet

import io.raspberrywallet.server.Server
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.grpc.VertxServerBuilder

internal fun main(args: Array<String>) {
    val server = Server(ManagerMockup())
    server.start()
    val service = object : GreeterGrpc.GreeterVertxImplBase(){
        override fun sayHello(request: HelloRequest?, response: Future<HelloReply>?) {
            val reply = HelloReply.newBuilder()
                .setMessage(request?.name)
                .build()
            response?.complete(reply)
        }
    }
    val vertxGRpc = Vertx.vertx()

    val grpcServer = VertxServerBuilder
        .forAddress(vertxGRpc, "127.0.0.1", 8081)
        .addService(service)
        .build()
        .start()


    println("gRPC Server started")
    Runtime.getRuntime().addShutdownHook(Thread { println("Ups, JVM shutdown") })
    grpcServer.awaitTermination()

    println("gRPC Server stopped")

}


