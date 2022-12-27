package com.jbmo60927.communication;

import java.util.Objects;

public class TypeList {

    private final Type[] types;

    public TypeList(String[] typesName) {
        types = new Type[typesName.length+1];
        types[0] = new Type("null", 0);
        for (int i = 0; i < typesName.length; i++) {
            types[i+1] = new Type(typesName[i], i+1);
        }
    }

    public int findValue(String name) {
        for (Type type : types) {
            if (Objects.equals(type.getName(), name))
                return type.getValue();
        }
        return -1;
    }
    
    public String findName(int value) {
        for (Type type : types) {
            if (type.getValue() == value)
                return type.getName();
        }
        return "";
    }
}
