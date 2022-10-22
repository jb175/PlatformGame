package com.jbmo60927.utilz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jbmo60927.utilz.Constants.PathConstants;

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

        readXMLFile();
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
    private void readXMLFile(){
        try {
            readFile();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cant read XML file", e);
        }
    }

    /**
     * read the file and store the properties.
     * @throws IOException exception if the file is not found
     */
    private void readFile() throws IOException {
        try (FileInputStream fis = new FileInputStream(PathConstants.PROJECT_ROOT+fileName)) {
            properties.loadFromXML(fis);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Cant open and save the XML file\nXML file regenerated", e);
            String path = "platform_client.jar/com/jbmo60927/utilz/client.xml";
            //path = "src/main/java/com/jbmo60927/utilz/client.xml";
            path = path.replace("platform_client.jar", "src/main/java");
            try (FileInputStream fis = new FileInputStream(PathConstants.PROJECT_ROOT+path)) {
                properties.loadFromXML(fis);
            } catch (FileNotFoundException e2) {
                LOGGER.log(Level.SEVERE, "Cant open the data stored XML file", e2);
            }
        }
        LOGGER.log(Level.CONFIG, "property file succesfully read");
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
        return readIntProperty(name, 0);
    }

    /**
     * read an integer property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public int readIntProperty(String name, int attempt) {
        final int maxAttempts = 2;
        if (attempt < maxAttempts) {
            try {
                return Integer.parseInt(readStringProperty(name));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "cannot parse string property into an integer. file is regenerated", e);
                deletePropertyFile();
                readXMLFile();
                return readIntProperty(name, attempt+1);
            }
        } else {
            LOGGER.log(Level.SEVERE, "default file has been modified");
            System.exit(1);
        }
        return 0;
    }

    /**
     * read a float property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public float readFloatProperty(String name) {
        return readFloatProperty(name, 0);
    }

    /**
     * read a float property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public float readFloatProperty(String name, int attempt) {
        final int maxAttempts = 2;
        if (attempt < maxAttempts) {
            try {
                return Float.parseFloat(readStringProperty(name));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "cannot parse string property into a float. file is regenerated", e);
                deletePropertyFile();
                readXMLFile();
                return readFloatProperty(name, attempt+1);
            }
        } else {
            LOGGER.log(Level.SEVERE, "default file has been modified");
            System.exit(1);
        }
        return 0f;
    }

    /**
     * read a boolean property
     * @param name the name of the property
     * @return return the value of the property or stop the server if the property is not found
     */
    public Boolean readBooleanProperty(String name) {
        return "true".equals(readStringProperty(name));
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

    public void saveBooleanProperty(String name, Boolean value) {
        if (Boolean.TRUE.equals(value))
            saveStringProperty(name, "true");
        else
            saveStringProperty(name, "false");
    }



    /**
     * read the file and store the properties.
     */
    public void saveFile() {
        try (FileOutputStream fos = new FileOutputStream(PathConstants.PROJECT_ROOT+fileName)) {
            // store the properties in the specific xml
            properties.storeToXML(fos, "client properties");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cant open and save the XML file", e);
        }
    }

    public void deletePropertyFile() {
        try {
            Files.delete(Paths.get(PathConstants.PROJECT_ROOT+fileName));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "XML file cant be reset", e);
        }
      }
}
