package com.jbmo60927.communication.types;

public class StringTypeList extends TypeList {

    private final StringType[] types;

    public StringTypeList(String[] typesName) {
        //we initalize the array corresponding to the number of given parameters
        this.types = new StringType[typesName.length+1];

        //always a null type for 0
        this.types[0] = new StringType("null");

        //complete all other values with the given strings
        for (int i = 0; i < typesName.length; i++) {
            this.types[i+1] = new StringType(typesName[i]);
        }
    }
    
    public StringTypeList(StringType[] typesName) {
        //we initalize the array corresponding to the number of given parameters
        this.types = new StringType[typesName.length+1];

        //always a null type for 0
        this.types[0] = new StringType("null");

        //complete all other values with the given strings
        for (int i = 0; i < typesName.length; i++) {
            this.types[i+1] = typesName[i];
        }
    }

    public String findName(int value) {
        return TypeList.findName(value, this.types);
    }

    public int findValue(String name) {
        return TypeList.findValue(name, this.types);
    }

    public StringType findType(int value) {
        return TypeList.findType(value, this.types);
    }

    public StringType findType(String name) {
        return TypeList.findType(name, this.types);
    }
}
