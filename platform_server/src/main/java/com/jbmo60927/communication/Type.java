package com.jbmo60927.communication;

import java.util.Objects;

public class Type {

    private final String name;
    private final int value;

    public Type(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
