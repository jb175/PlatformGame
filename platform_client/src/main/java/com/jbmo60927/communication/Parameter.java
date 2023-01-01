package com.jbmo60927.communication;

import com.jbmo60927.communication.types.StringType;
import java.util.Objects;

public class Parameter {

    //header structure
	public static final int PARAMETER_TYPE_BYTES = 1;
	public static final int PARAMETER_SIZE_BYTES = 1;

    private StringType parameterType;
    private byte[] value;

    public Parameter(StringType parameterType, byte[] value) {
        this.parameterType = parameterType;
        this.value = value;
    }

    public StringType getParameterType() {
        return parameterType;
    }

    public byte[] getValue() {
        return value;
    }

    public static byte[] getParameter(Parameter[] parameters, StringType parameterType) {
        for (Parameter parameter : parameters) {
            if (Objects.equals(parameter.getParameterType().getTypeName(), parameterType.getTypeName()))
                return parameter.getValue();
        }
        return new byte[] {};
    }

    public static int hasParameter(Parameter[] parameters, StringType parameterType) {
        for (int i = 0; i < parameters.length; i++) {
            if (Objects.equals(parameters[i].getParameterType().getTypeName(), parameterType.getTypeName()))
                return i;
        }
        return -1;
    }
}