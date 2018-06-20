package io.raspberrywallet;

import io.raspberrywallet.module.Module;
import io.raspberrywallet.module.ModuleState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Manager {
    String ping(); // for DEBUG purposes
    List<Module> getModules();
    ModuleState getModuleState(@NotNull String moduleId);
    Response nextStep(@NotNull String moduleId, byte[] input); // pass input for current step and return next step


}
