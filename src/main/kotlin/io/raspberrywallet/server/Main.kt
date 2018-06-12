package io.raspberrywallet.server

import io.vertx.core.Vertx

fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    vertx.setPeriodic(1000){
        println("Hello")
    }
}
