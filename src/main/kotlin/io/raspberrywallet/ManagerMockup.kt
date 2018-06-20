package io.raspberrywallet

import io.raspberrywallet.module.Module
import io.raspberrywallet.module.ModuleState
import io.raspberrywallet.step.SimpleStep
import java.util.*

class ManagerMockup : Manager {
    override fun getAddress(): ByteArray {
        return "133asfafs6fd6xvz67zvx55sad6f5fd".toByteArray()
    }

    override fun restoreFromBackupPhrase(mnemonicWords: MutableList<String>) {

    }


    override fun getModules(): MutableList<Module> {
        val module = object : Module("Module1") {}
        val module2 = object : Module("Module2") {}
        val module3 = object : Module("Button") {}
        return mutableListOf(module, module2, module3)
    }

    override fun getModuleState(moduleId: String): ModuleState = ModuleState.WAITING

    override fun nextStep(moduleId: String, input: ByteArray?): Response =
        if (Random().nextBoolean())
            Response(SimpleStep("Do something"),
                Response.Status.OK)
        else
            Response(null,
                Response.Status.FAILED)

    override fun ping(): String = "pong"
}
