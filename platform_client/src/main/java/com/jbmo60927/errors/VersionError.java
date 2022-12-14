package com.jbmo60927.errors;

public class VersionError extends Error {

    /**
     * Version error that occure when the client and the server does'nt use the same version
     */
    public VersionError() {
        super("client version is not the one requested to connect to the server");
    }

    public VersionError(String message) {
        super(message);
    }
}