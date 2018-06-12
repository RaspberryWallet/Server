package io.raspberrywallet

class ManagerMockup : BackendProvider {
    override fun ping(): String = "pong"
}
