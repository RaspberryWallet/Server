package io.raspberrywallet.server

import com.stasbar.Logger
import io.raspberrywallet.Manager
import io.raspberrywallet.grpc.*
import io.raspberrywallet.module.Module
import io.vertx.core.Future
import io.vertx.grpc.GrpcWriteStream

fun Module.toGrpcModule(): io.raspberrywallet.grpc.Module {
    return io.raspberrywallet.grpc.Module.newBuilder()
        .setId(id)
        .setName(name)
        .setDescription(description)
        .build()
}

fun io.raspberrywallet.module.ModuleState.toGrpcModuleState(): io.raspberrywallet.grpc.ModuleState {
    return io.raspberrywallet.grpc.ModuleState.newBuilder()
        .setMessage(message)
        .setState(name)
        .build()
}

internal class GrpcVerticle(private val manager: Manager) : RaspberryWalletServerGrpc.RaspberryWalletServerVertxImplBase() {

    override fun ping(request: Empty?, response: Future<PingResponse>?) {
        val reply = PingResponse.newBuilder()
            .setResponse("pong")
            .build()
        Logger.d("Say ${reply.response}")
        response?.complete(reply)
    }

    override fun getModules(request: Empty?, response: Future<ModulesResponse>?) {
        val replyBuilder = ModulesResponse.newBuilder()

        manager.modules.forEach {
            replyBuilder.addModules(it.toGrpcModule())
            Logger.d("Module ${it.id} ${it.name} ${it.description}")
        }
        response?.complete(replyBuilder.build())
    }

    override fun getModuleState(request: ModuleRequest?, response: GrpcWriteStream<ModuleState>?) {
        response?.write(manager.getModuleState(request!!.moduleId).toGrpcModuleState())
        response?.write(manager.getModuleState(request!!.moduleId).toGrpcModuleState())
        response?.write(manager.getModuleState(request!!.moduleId).toGrpcModuleState())
    }
}
