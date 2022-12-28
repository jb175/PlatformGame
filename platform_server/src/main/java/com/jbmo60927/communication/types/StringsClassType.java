package com.jbmo60927.communication.types;

public class StringsClassType  extends StringClassType {

    String[] typeOtherNames;

    public StringsClassType(String[] typeNames, Class<?> typeClass) {
        super(typeNames[0], typeClass);
        this.typeOtherNames = new String[typeNames.length-1];
        for (int i = 0; i < typeNames.length-1; i++) {
            this.typeOtherNames[i] = typeNames[i+1];
        }
    }

    public String[] getTypeOtherNames() {
        return typeOtherNames;
    }
}
