package com.jbmo60927.utilz;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to read a property file.
 */
public class PropertyFile {
    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(PropertyFile.class.getName());
    //to store the properties of the file
    private final Properties properties = new Properties();
    
    /**
     * try to read and store the file properties and log the results.
     * @param filePath
     */
    public PropertyFile(final String filePath) {
        LOGGER.setLevel(Level.CONFIG); //we wants to see config log

        try {
            readFile(filePath);
            LOGGER.log(Level.CONFIG, "property file succesfully read");
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "property file not found", e);
        }
    }

    /**
     * permitt to access the read properties from outside of the class.
     * @return
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * read the file and store the properties.
     * @param filePath path of the file into the jar (com.(...))
     * @throws IOException exception if the file is not found
     */
    private void readFile(final String filePath) throws IOException{
        properties.loadFromXML(this.getClass().getClassLoader().getResourceAsStream(filePath));
    }
}
