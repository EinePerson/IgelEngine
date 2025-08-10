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
     * @see #Config(String) Config
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
     * Gets a value from the config, the returned value is normally of type String
     * @param key the key of the Item
     * @return the value, this might be null
     * @see #getOrDefault(String, Object)
     */
    public Object get(String key){
        return properties.get(key);
    }

    /**
     * Gets a value from the config, if the value is neither a String nor a Primitive, it has to be converted from the returned String
     * @param key the key of the Item
     * @param defaultValue the value to return when no value is found
     * @return the value that is read or the default value
     * @see #getOrDefault(String, Object)
     */
    public Object getOrDefault(String key,Object defaultValue){
        String value = properties.getOrDefault(key,defaultValue).toString();
        if (defaultValue instanceof Integer) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }else if(defaultValue instanceof Short){
            try {
                return Short.parseShort(value);
            }catch (NumberFormatException e){
                return defaultValue;
            }
        }else if(defaultValue instanceof Byte){
            try {
                return Byte.parseByte(value);
            }catch (NumberFormatException e){
                return defaultValue;
            }
        }else if(defaultValue instanceof Character){
            return value.charAt(0);
        } else if (defaultValue instanceof Boolean) {
            return Boolean.parseBoolean(value);
        } else if (defaultValue instanceof Double) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else if (defaultValue instanceof Long) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else if (defaultValue instanceof Float) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return value;
    }

    /**
     * Writes the specific value as String
     * @param key the key that identifies the value
     * @param value the value to write
     */
    public void write(String key,Object value){
        try {
            properties.setProperty(key,value.toString());
            properties.store(new FileOutputStream(name),null);
            properties.load(new FileInputStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks weather a specific key is present in the config
     * @param key the key to check for
     * @return weather the key is present
     */
    public boolean contains(String key){
        return properties.containsKey(key);
    }
}
