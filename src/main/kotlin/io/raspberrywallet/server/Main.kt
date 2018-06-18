package io.raspberrywallet.server

import io.raspberrywallet.ManagerMockup

fun main(args: Array<String>) {
    val server = Server(ManagerMockup())
    server.start()
}


