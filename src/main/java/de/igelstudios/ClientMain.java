package de.igelstudios;

import de.igelstudios.igelengine.client.lang.ClientConfig;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Client");
    private static ClientMain instance;

    public static synchronized ClientMain getInstance() {
        return instance;
    }

    public static void main(String args[]) {
        new ClientMain(ClientMain.class).start();
    }

    public ClientEngine getEngine() {
        return engine;
    }

    private ClientEngine engine;
    private ClientConfig config;
    private final Class<?> clazz;

    public ClientMain(Class<?> clazz){
        this.clazz = clazz;
        instance = this;
        config = new ClientConfig();
        engine = new ClientEngine();
    }

    public void start(){
        engine.start();
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
