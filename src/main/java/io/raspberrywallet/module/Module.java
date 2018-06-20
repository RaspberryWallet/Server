package io.raspberrywallet.module;

import java.util.UUID;

public abstract class Module {
    private final String name;
    private final String id = UUID.randomUUID().toString();

    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
