package com.jbmo60927.command;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.App;
import com.jbmo60927.communication.Parameter;
import com.jbmo60927.communication.types.StringTypeList;

import static com.jbmo60927.utilz.HelpsMethods.stringToBytes;

public abstract class Command {

    private static final String BEGINCLASSPATH = "com.jbmo60927.command";
    private static final String COMMANDSEP = " ";
    private static final String COMMANDPARAMETERSEP = ":";
    private static final String TYPELISTFIELDNAME = "TYPES";

    protected static final Logger LOGGER = Logger.getLogger(Command.class.getName()); //logger for this class
    protected Parameter[] parameters;

    protected Command(Parameter[] parameters) {
        LOGGER.setLevel(Level.INFO);
        this.parameters = parameters;
    }

    public abstract String execute(App app);

    private static final Parameter[] readCommandParameters(String command, Class<?> commandClass) throws IllegalArgumentException, IllegalAccessException, SecurityException {
        ArrayList<Parameter> parameters = new ArrayList<>();

        //has parameters
        if (command.contains(Command.COMMANDSEP)) {
            String[] rawParameters = command.substring(command.indexOf(Command.COMMANDSEP)+1).split(Command.COMMANDSEP);
            
            String parameterName;
            String parameterValue;
            for (int i = 0; i < rawParameters.length; i++) {
                try {
                    if (rawParameters[i].contains(Command.COMMANDPARAMETERSEP)) {
                        parameterName = rawParameters[i].split(Command.COMMANDPARAMETERSEP)[0];
                        parameterValue = rawParameters[i].split(Command.COMMANDPARAMETERSEP)[1];

                        if (((StringTypeList) commandClass.getDeclaredField(Command.TYPELISTFIELDNAME).get(null)).findValue(parameterName) != -1)
                            parameters.add(new Parameter(((StringTypeList) commandClass.getDeclaredField(Command.TYPELISTFIELDNAME).get(commandClass)).findType(parameterName), stringToBytes(parameterValue)));
                    }
                } catch (NoSuchFieldException e) {
                    LOGGER.log(Level.SEVERE, String.format("TypeList not found for command %s", commandClass.getName()), e);
                }
            }
        }

        return parameters.toArray(new Parameter[parameters.size()]);
    }
    
    public static final Command readCommand(String command) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        
        String commandName = command.split(Command.COMMANDSEP)[0];
        Class<?> commandClass = Class.forName(String.format(Command.BEGINCLASSPATH+".%s", commandName.substring(0, 1).toUpperCase()+commandName.substring(1).toLowerCase()));
        
        Parameter[] parameters = Command.readCommandParameters(command, commandClass);

        return (Command) commandClass //path of classes
            .getDeclaredConstructor(Parameter[].class) // we take the first constructor (only one permitted)
            .newInstance(new Object[] {parameters});
    }

    // public Type[] getAllParameterTypes() throws SecurityException, IllegalArgumentException, IllegalAccessException {
    //     Type[] types = new Type[this.getClass().getDeclaredClasses()[0].getDeclaredFields().length];
    //     for (int i = 0; i < types.length; i++) {
    //         types[i] = (Type) this.getClass().getDeclaredClasses()[0].getDeclaredFields()[i].get(this.getClass().getDeclaredClasses()[0]);
    //     }
    //     return types;
    // }
}