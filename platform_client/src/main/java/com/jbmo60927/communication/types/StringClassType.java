package com.jbmo60927.communication.types;

public class StringClassType extends StringType{

    Class<?> typeClass;

    public StringClassType(String typeName, Class<?> typeClass) {
        super(typeName);
        this.typeClass = typeClass;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }
}
