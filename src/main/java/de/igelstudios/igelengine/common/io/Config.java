package de.igelstudios.igelengine.common.io;

import java.io.*;
import java.util.Objects;
import java.util.Properties;


/**
 * This class can handle default .properties files;
 */
public final class Config {
    private final Properties properties;
    private final String name;

    /**
     * Creates a new instance of Config
     * @param name the name of the file
     */
    public Config(String name){
        this(name,false);
    }

    /**
     * Creates a new instance of Config
     * @param name the name of the file
     * @param withType weather the name includes the file extension
     * @see #Config(String)  Config
     */
    public Config(String name,boolean withType){
        if(!withType) name += ".properties";
        this.name = name;
        properties = new Properties();
        try {
            if(!new File(name).exists())new File(name).createNewFile();
            properties.load(new FileInputStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a value from the config
     * @param key the key of the Item
     * @return the value, this might be null
     */
    public Object get(String key){
        return properties.get(key);
    }

    /**
     * Gets a value from the Config
     * @param key the key of the Item
     * @param standard the Value of the Item of there is non associated in the file
     * @return the value of the propertied or standard
     */
    public Object get(String key, Object standard){
        return properties.getOrDefault(key,standard);
    }

    public void write(String key,Object value){
        try {
            properties.setProperty(key,value.toString());
            properties.store(new FileOutputStream(name),null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
