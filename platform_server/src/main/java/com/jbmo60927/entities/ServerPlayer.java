package com.jbmo60927.entities;

import com.jbmo60927.thread.ServiceThread;

public class ServerPlayer extends Player{

    private final ServiceThread serviceThread;

    public ServerPlayer(ServiceThread serviceThread) {
        super(0, 0, -1, "default name");
        this.serviceThread = serviceThread;
    }

    public ServiceThread getServiceThread() {
        return serviceThread;
    }
}
