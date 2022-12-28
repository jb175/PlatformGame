package com.jbmo60927.communication.types;

public class StringsClassTypeList extends TypeList {

    private final StringsClassType[] types;
    
    public StringsClassTypeList(StringsClassType[] types) {
        //we initalize the array corresponding to the number of given parameters
        this.types = new StringsClassType[types.length+1];

        //always a null type for 0
        this.types[0] = new StringsClassType(new String[] {"null"}, String.class);

        //complete all other values with the given strings
        for (int i = 0; i < types.length; i++) {
            this.types[i+1] = types[i];
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

    public StringsClassType findType(String name) {
        return TypeList.findType(name, this.types);
    }
}
