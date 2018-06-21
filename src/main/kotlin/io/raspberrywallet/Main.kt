package io.raspberrywallet

import io.raspberrywallet.server.Server

fun main(args: Array<String>) {
    val server = Server(ManagerMockup())
    server.start()
}


