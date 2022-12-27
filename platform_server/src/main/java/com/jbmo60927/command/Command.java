package com.jbmo60927.command;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.communication.Type;
import com.jbmo60927.communication.TypeList;
import com.jbmo60927.communication.parameters.Parameter;

import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

public abstract class Command {

    protected static final Logger LOGGER = Logger.getLogger(Command.class.getName()); //logger for this class
    protected Parameter[] parameters;

    protected Command(Parameter[] parameters) {
        LOGGER.setLevel(Level.INFO);
        this.parameters = parameters;
    }

    public abstract String execute();
    
    public static final Command readCommand(String command) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        String commandName = command.split(" ")[0];
        String[] rawParameters = command.substring(command.indexOf(" ")+1).split(" ");
        ArrayList<Parameter> parameters = new ArrayList<>();
        Class<?> commandClass = Class.forName(String.format("com.jbmo60927.command.%s", commandName.substring(0, 1).toUpperCase()+commandName.substring(1).toLowerCase()));
        
        String parameterName;
        String parameterValue;
        for (int i = 0; i < rawParameters.length; i++) {
            try {
                parameterName = rawParameters[i].split(":")[0];
                parameterValue = rawParameters[i].split(":")[1];

                if (((TypeList) commandClass.getDeclaredField("TYPES").get(null)).findValue(parameterName) != -1)
                    parameters.add(new Parameter(((TypeList) commandClass.getDeclaredField("TYPES").get(commandClass)).findValue(parameterName), stringToBytes(parameterValue)));
                
            } catch (NoSuchFieldException e) {
                LOGGER.log(Level.SEVERE, String.format("TypeList not found for command %s", commandClass.getName()), e);
            }
        }

        return (Command) commandClass //path of classes
            .getDeclaredConstructor(Parameter[].class) // we take the first constructor (only one permitted)
            .newInstance(new Object[] {parameters.toArray(new Parameter[parameters.size()])});
    }

    public Type[] getAllParameterTypes() throws SecurityException, IllegalArgumentException, IllegalAccessException {
        Type[] types = new Type[this.getClass().getDeclaredClasses()[0].getDeclaredFields().length];
        for (int i = 0; i < types.length; i++) {
            types[i] = (Type) this.getClass().getDeclaredClasses()[0].getDeclaredFields()[i].get(this.getClass().getDeclaredClasses()[0]);
        }
        return types;
    }
}