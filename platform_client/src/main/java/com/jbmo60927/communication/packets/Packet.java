package com.jbmo60927.communication.packets;

import static com.jbmo60927.utilz.HelpsMethods.bytesToInt;
import static com.jbmo60927.utilz.HelpsMethods.intToBytes;
import static com.jbmo60927.utilz.HelpsMethods.subByteArray;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringType;
import com.jbmo60927.communication.types.StringTypeList;


public abstract class Packet {

    //path to find all packet classes
    private static final String BEGINCLASSPATH = "com.jbmo60927.communication.packets";
    //packet classes field name for the parameter types
    private static final String TYPELISTFIELDNAME = "TYPES";

    //packet types list
    protected static final StringTypeList TYPES = new StringTypeList(new String[] {
			"Reception", "Welcome", "Version", "Join", "Quit", "Say",
			"NewJoiner", "Position", "RemovePlayer", "NewEntity", "Setname",
			"SetLevel", "RemoveLevel", "ChangeLevel"}
		);


	//size for each packet header field
	public static final int PACKET_TYPE_BYTES = 1; //type of the packet
	public static final int PACKET_PARAMETER_NUMBER_BYTES = 1; //number of parameter a packet has after the header

	//the type of the packet
    protected final StringType packetType;
	//the parameters of the packet
    protected final Parameter[] parameters;

	//the logger
    protected static final Logger LOGGER = Logger.getLogger(Packet.class.getName());

	/**
	 * the common part of all packets
	 * @param packetType the type of the packet
	 * @param packetSize the size of the packet
	 * @param parameters the parameters we have on this packet
	 */
    protected Packet(final StringType packetType, final Parameter[] parameters) {
        LOGGER.setLevel(Level.INFO);
		this.packetType = packetType;
        this.parameters = parameters;
    }

    //each packet can be executed. It's where we modify the game with the data transfer between client/server
    public abstract void execute();

	/**
	 * to access the value of the packet outside of himself
	 * @return the packet parameters
	 */
    public Parameter[] getParameters() {
        return parameters;
    }

	/**
	 * to access the value of the packet type outside of himself
	 * @return the packet parameters
	 */
	public StringType getPacketType() {
        return packetType;
    }

    /**
     * add a parameter to a parameter array
     * @param parameters the array of parameter on wich we wants to add a new parameter
     * @param parameter the parameter we wants to add
     * @return the array with the parameter
     */
    public static Parameter[] addParameter(Parameter[] parameters, Parameter parameter) {

        //if there is already a parameter like tyhis one on the array we get is position
        int indexOfType = Parameter.hasParameter(parameters, parameter.getParameterType());

        //and we modify the value
        if (indexOfType >= 0 && indexOfType < parameters.length) {
            parameters[indexOfType] = parameter;
            return parameters;

        //otherwise we need to create a new temporary array to contain the new parameter
        } else if (indexOfType == -1) {

            //we create the array
            Parameter[] newParameters = new Parameter[parameters.length+1];

            //we copy it from the old one
            int c = 0;
            for (Parameter p : parameters)
                newParameters[c++] = p;

            //we add the new paramter
            newParameters[c] = parameter;

            //and we return the new array
            return newParameters;
        }

        //if something goes wrong we display an error and return the old array
        LOGGER.log(Level.SEVERE, () -> String.format("can't add parameter (%s pos:%d)", parameter.getParameterType().getTypeName(), indexOfType));
        return parameters;
    }

    /**
     * return the value we should use for a given parameter (the one receive from the packet or a default one)
     * @param parameterType type of the parameter we wants to search the value
     * @param parameters parameters from the packet we receive
     * @param defaultValue default value if the parameter is not defined
     * @return the value we should use for this parameter
     */
    protected static byte[] getParameterValue(final StringType parameterType, final Parameter[] parameters, final byte[] defaultValue) {
        
        //we try to get the value from the packet we receive
        final byte[] parameterValue = Parameter.getParameter(parameters, parameterType);

        //if something was return we return this value
        if (parameterValue != new byte[] {})
            return parameterValue;
        
        //if nothing is returned we return the default value
        else
            return defaultValue;
    }

    /**
     * compact the packet into a byte array to send him
     * @param packet the packet we should compact
     * @return the byte array we need to send
     * @throws IllegalArgumentException exception that could occure
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws ClassNotFoundException
     */
    public static byte[] getCompactPacket(final Packet packet) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {

        //calculate the total size of the packet
		int totalSize = Packet.PACKET_TYPE_BYTES+Parameter.PARAMETER_SIZE_BYTES;
		for (final Parameter parameter : packet.getParameters()) {
			totalSize += (Parameter.PARAMETER_TYPE_BYTES+Parameter.PARAMETER_SIZE_BYTES+parameter.getValue().length);
		}

        //create the array
        final byte[] compactPacket = new byte[totalSize];
        
        //value to write on the array easily
        int writeByte = 0;


        //the type of this packet
		final byte[] packetType = intToBytes(Packet.TYPES.findValue(packet.packetType), Packet.PACKET_TYPE_BYTES);
        //the number of arguments this packet has
		final byte[] parameterNumber = intToBytes(packet.parameters.length, Parameter.PARAMETER_SIZE_BYTES);
        //the class of the packet
        final Class<?> packetClass = Class.forName(packet.getClass().getName());

        //parameter value to easily write data
        byte[] parameterType;
        byte[] parameterSize;

        //we write all the bytes from packet type into the array 
        for (final byte b : packetType)
            compactPacket[writeByte++] = b;
        //we write all the bytes from packet parameter number into the array 
        for (final byte b : parameterNumber)
            compactPacket[writeByte++] = b;

        //for each parameter
        for (final Parameter parameter : packet.parameters) {

            //we get the parameter type
            parameterType = intToBytes(((StringTypeList) packetClass.getDeclaredField(Packet.TYPELISTFIELDNAME).get(packetClass)).findValue(parameter.getParameterType()), Parameter.PARAMETER_TYPE_BYTES);
            //we get the parameter size
            parameterSize = intToBytes(parameter.getValue().length, Parameter.PARAMETER_SIZE_BYTES);

            //we write all the bytes from parameter type into the array 
            for (final byte b : parameterType)
                compactPacket[writeByte++] = b;
            //we write all the bytes from parameter size into the array 
            for (final byte b : parameterSize)
                compactPacket[writeByte++] = b;
            //we write all the bytes from parameter data into the array 
            for (final byte b : parameter.getValue())
                compactPacket[writeByte++] = b;
        }

        //we return the array
        return compactPacket;
	}

    /**
     * this method read the byte array from the client/server
     * @param is the input stream to read the data from
     * @return a byte array with the byte packet
     * @throws IOException an exception that could occure with the input stream
     */
    public static byte[] readRawPacket(final InputStream is) throws IOException {

        //value to store what we have already written into the rawPaquets
        int rawPacketWrite = 0;
        //the byte array we will return at the end
        byte[] rawPacket = new byte[Packet.PACKET_TYPE_BYTES+Packet.PACKET_PARAMETER_NUMBER_BYTES];
        //an other array to modify the other array without loosing what is inside (we duplicate the array into a bigger one each time we read a new parameter)
        byte[] rawPacketBuilder;

        //we read the packet type
        final byte[] packetType = is.readNBytes(Packet.PACKET_TYPE_BYTES);
        //we read the number of parameter this packet has
        final byte[] packetNumberParameter = is.readNBytes(Packet.PACKET_PARAMETER_NUMBER_BYTES);
        //an integer for the number of parameter to use it to read all parameters
        final int nBytes = bytesToInt(packetNumberParameter);


        //we store all the bytes from packet type into the array
        for (final byte b : packetType)
            rawPacket[rawPacketWrite++] = b;
        //we store all the bytes from packet nuber parameters into the array 
        for (final byte b : packetNumberParameter)
            rawPacket[rawPacketWrite++] = b;

        //foreach parameters
        for (int i = 0; i < nBytes; i++) {

            //we read the parameter type
            final byte[] parameterType = is.readNBytes(Parameter.PARAMETER_TYPE_BYTES);
            //we read the parameter data size
            final byte[] parameterSizeData = is.readNBytes(Parameter.PARAMETER_SIZE_BYTES);
            //we convert this size into an int to use it
            final int parameterSize = bytesToInt(parameterSizeData);
            //we read parameter data
            final byte[] parameterValue = is.readNBytes(parameterSize);

            //we reset the value to recreate a new array
            rawPacketWrite = 0;
            //we reset the builder array to the new size (size of the old array plus the size of all the data from this parameter)
            rawPacketBuilder = new byte[rawPacket.length+Parameter.PARAMETER_TYPE_BYTES+Parameter.PARAMETER_SIZE_BYTES+parameterSize];


            //we store all the bytes from the old array into the array
            for (final byte b : rawPacket) {
                rawPacketBuilder[rawPacketWrite++] = b;
            }
            //we store all the bytes from the parameter type into the array
            for (final byte b : parameterType) {
                rawPacketBuilder[rawPacketWrite++] = b;
            }
            //we store all the bytes from the parameter data size into the array
            for (final byte b : parameterSizeData) {
                rawPacketBuilder[rawPacketWrite++] = b;
            }
            //we store all the bytes from the parameter data into the array
            for (final byte b : parameterValue) {
                rawPacketBuilder[rawPacketWrite++] = b;
            }

            //store the packetBuilder into the other array
            rawPacket = new byte[rawPacketBuilder.length];
            for (int j = 0; j < rawPacketBuilder.length; j++) {
                rawPacket[j] = rawPacketBuilder[j];
            }
        }

        //return the array with all bytes
        return rawPacket;
    }

    /**
     * read the packet parameters from the byte array
     * @param packetClass the class of the packet we are reading
     * @param rawPacket the byte array of the packet
     * @param read the int from where we have already read data (we continue from readPacket)
     * @param packetParameterNumber the number of parameters we should read into this array
     * @return an array containing the parameters from the packet
     * @throws IOException exception that could occure
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     */
    private static Parameter[] readPacketParameters(final Class<?> packetClass, final byte[] rawPacket, int read, final int packetParameterNumber) throws IOException, IllegalArgumentException, IllegalAccessException, SecurityException {
        
        //th array to contain all parameters
        final Parameter[] parameters = new Parameter[packetParameterNumber];
        
        //parameter type (int)
        int parmameterRawType;
        //parameter type
        StringType parameterType;
        //parameter size
        int parameterSize;
        //parameter vale
        byte[] parameterValue;

        //foreach parameter (if 0 it will return an empty array)
        for (int i = 0; i < packetParameterNumber; i++) {

            //we try to read the parameter
            try {

                //read the parameter type
                parmameterRawType = bytesToInt(subByteArray(rawPacket, read, Parameter.PARAMETER_TYPE_BYTES)); //int type
                parameterType = ((StringTypeList) packetClass.getDeclaredField(Packet.TYPELISTFIELDNAME).get(packetClass)).findType(parmameterRawType); //convert the type using the typelist
                read+=Parameter.PARAMETER_TYPE_BYTES; //increase the read value

                //read the parameter size
                parameterSize = bytesToInt(subByteArray(rawPacket, read, Parameter.PARAMETER_SIZE_BYTES));
                read+=Parameter.PARAMETER_SIZE_BYTES;

                //parameter value
                parameterValue = subByteArray(rawPacket, read, parameterSize);
                read+=parameterSize;
                
                //we add the parameter to the array
                parameters[i] = new Parameter(parameterType, parameterValue);
                
            //if something happen it will raise an exception
            } catch (final NoSuchFieldException e) {
                LOGGER.log(Level.SEVERE, String.format("TypeList not found for command %s", packetClass.getName()), e);
            }
        }

        //return the array
        return parameters;
    }
    
    /**
     * create a packet from the byte array containing a row packet
     * @param rawPacket the byte array containing the packet
     * @param app the app to use it into packets
     * @return the packet
     */
    public static final Packet readPacket(final byte[] rawPacket, final App app) {
		try {
            //a field to read all the raw packet without forgeting a value
            int read = 0;

            //read the packet type
            final int packetType = bytesToInt(subByteArray(rawPacket, read, Packet.PACKET_TYPE_BYTES)); //read the value
            final String packetName = Packet.TYPES.findName(packetType); //convert the packet type using the typelist
            read+=Packet.PACKET_TYPE_BYTES; //increment the read value


            //read the packet number of parameters
            final int packetParameterNumber = bytesToInt(subByteArray(rawPacket, read, Packet.PACKET_PARAMETER_NUMBER_BYTES));
            read+=Packet.PACKET_PARAMETER_NUMBER_BYTES;

            //if the packet is not of the type null
            if (!"null".equals(packetName)) {
                //we set the packet class
                final Class<?> packetClass = Class.forName(String.format(Packet.BEGINCLASSPATH+".%s", packetName));

                //we get the parameters using the dedicated method
                final Parameter[] packetParameters = readPacketParameters(packetClass, rawPacket, read, packetParameterNumber);
                
                //we return the packet with the parameters and the app as parameters
                try {
                    return (Packet) packetClass //path of classes
                        .getDeclaredConstructor(Parameter[].class, App.class) // we try to find a constructor with a parameter array and an app as parameters
                        .newInstance(new Object[] {packetParameters, app}); //the constructor require the parameter array and the app
                
                //if it doesn't work, we try again without the app
                } catch (NoSuchMethodException e) {
                    return (Packet) packetClass //path of classes
                        .getDeclaredConstructor(Parameter[].class) // we try to find a constructor with a parameter array as parameters
                        .newInstance(new Object[] {packetParameters}); //the constructor only require the parameter array
                }
            }
        
        //if an error occure
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | IOException e) {
            LOGGER.log(Level.SEVERE, "error while reading the packet (possibly an error betwwen Packets.TYPES between client/server", e);
        }

        //return an empty packet
		return null;
    }
}