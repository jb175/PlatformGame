package com.jbmo60927.utilz;

import java.io.FileOutputStream;
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

    private final String fileName;
    
    /**
     * try to read and store the file properties and log the results.
     * @param filePath
     */
    public PropertyFile(final String filePath) {
        fileName = filePath;

        LOGGER.setLevel(Level.CONFIG); //we wants to see config log

        try {
            readFile();
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
     * @throws IOException exception if the file is not found
     */
    private void readFile() throws IOException{
        properties.loadFromXML(this.getClass().getClassLoader().getResourceAsStream(fileName));
    }

    /**
     * read a string property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public String readStringProperty(String name) {
        if (properties.getProperty(name) != null) {
            LOGGER.log(Level.CONFIG, () -> String.format("%s sucessfully read", name));
            return properties.getProperty(name);
        } else {
            LOGGER.log(Level.SEVERE, () -> String.format("%s cannot be read on the server.xml file", name));
            System.exit(1);
        }
        return "";
    }

    /**
     * read an integer property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public int readIntProperty(String name) {
        try {
            return Integer.parseInt(readStringProperty(name));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "cannot parse string property into an integer", e);
        }
        return 0;
    }

    /**
     * read a float property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public float readFloatProperty(String name) {
        try {
            return Float.parseFloat(readStringProperty(name));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "cannot parse string property into a float", e);
        }
        return 0f;
    }

    public void saveStringProperty(String name, String value) {
        try {
            properties.setProperty(name, value);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error while saving a value in XML file", e);
        }
    }

    public void saveIntProperty(String name, int value) {
        saveStringProperty(name, Integer.toString(value));
    }

    public void saveFloatProperty(String name, Float value) {
        saveStringProperty(name, Float.toString(value));
    }



    /**
     * read the file and store the properties.
     */
    public void saveFile() {
        try {
        } catch (Exception e) {
        }
        try (FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"/src/main/java/"+fileName)) {
        //try (FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir")+"/platform_client.jar/"+fileName)) {
            // store the properties in the specific xml
            properties.storeToXML(fos, "client properties");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Can't open and save the XML file", e);
            e.printStackTrace();
        }
    }
}
