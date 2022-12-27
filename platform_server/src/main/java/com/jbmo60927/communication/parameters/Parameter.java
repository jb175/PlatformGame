package com.jbmo60927.communication.parameters;

public class Parameter {

    //header structure
	public static final int PARAMETER_TYPE_BYTES = 1;
	public static final int PARAMETER_SIZE_BYTES = 1;

    private int parameterType;
    private byte[] value;

    public Parameter(int parameterType, byte[] value) {
        this.parameterType = parameterType;
        this.value = value;
    }

    public int getParameterType() {
        return parameterType;
    }

    public byte[] getValue() {
        return value;
    }

    public static byte[] hasParameter(Parameter[] parameters, int parameterType) {
        for (Parameter parameter : parameters) {
            if (parameter.getParameterType() == parameterType)
                return parameter.getValue();
        }
        return new byte[] {};
    }
}