package com.jbmo60927.errors;

public class UnknowPacketError extends Error {
    public UnknowPacketError(String message, Throwable cause) {
        super(message, cause);
    }
    public UnknowPacketError(String message) {
        super(message);
    }
    public UnknowPacketError() {
        super("The packet receive is not recognize");
    }
}
