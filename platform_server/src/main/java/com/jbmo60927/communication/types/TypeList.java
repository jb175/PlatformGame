package com.jbmo60927.communication.types;

import java.util.Objects;

public abstract class TypeList {
    protected TypeList() {
    }
    /*
    public static String findName(int value, StringType[] types)
    public static int findValue(String name, StringType[] types)
    public static int findValue(String name, StringsClassType[] types)
    public static StringType findType(int value, StringType[] types)
    public static StringType findType(String name, StringType[] types)
    public static StringsClassType findType(String name, StringsClassType[] types)
     */
    
    protected static String findName(int value, StringType[] types) {
        if (value>=0 && value<types.length)
            return types[value].getTypeName();
        return null;
    }

    protected static int findValue(String name, StringType[] types) {
        for (int i = 0; i < types.length; i++)
            if (Objects.equals(name, types[i].getTypeName()))
                return i;
        return -1;
    }
    
    protected static int findValue(String name, StringsClassType[] types) {
        for (int i = 0; i < types.length; i++)
            if (Objects.equals(name, types[i].getTypeName()))
                return i;
        
        for (int i = 0; i < types.length; i++) {
            String[] otherNames = types[i].getTypeOtherNames();
            for (int j = 0; j < otherNames.length; j++)
                if (Objects.equals(name, otherNames[j]))
                    return i;
        }

        return -1;
    }
    
    protected static StringType findType(int value, StringType[] types) {
        if (value>=0 && value<types.length)
            return types[value];
        return null;
    }

    protected static StringType findType(String name, StringType[] types) {
        for (StringType stringType : types)
            if (Objects.equals(name, stringType.getTypeName()))
                return stringType;
        return null;
    }

    protected static StringsClassType findType(String name, StringsClassType[] types) {
        for (StringsClassType stringType : types)
            if (Objects.equals(name, stringType.getTypeName()))
                return stringType;
        
        for (StringsClassType stringType : types) {
            String[] otherNames = stringType.getTypeOtherNames();
            for (int i = 0; i < otherNames.length; i++)
                if (Objects.equals(name, otherNames[i]))
                    return stringType;
        }

        return null;
    }
}
