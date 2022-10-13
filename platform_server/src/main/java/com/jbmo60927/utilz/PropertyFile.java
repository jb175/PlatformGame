package com.jbmo60927.utilz;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyFile {
    //logger for this class
    private static final Logger LOGGER = Logger.getLogger(PropertyFile.class.getName());
    
    private final Properties properties = new Properties();
    
    public PropertyFile(String path) {
        LOGGER.setLevel(Level.INFO);

        LOGGER.log(Level.INFO, Thread.currentThread().getContextClassLoader().getResource("").getPath());

        try {
            readFile(path);
            LOGGER.log(Level.INFO, "property file succesfully read");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "xml file not found", e);
        }
    }

    private void readFile(String fileName) throws IOException{
        properties.loadFromXML(new FileInputStream(Thread.currentThread().getContextClassLoader().getResource("").getPath()+fileName));
    }

    public Properties getProperties() {
        return properties;
    }
}
