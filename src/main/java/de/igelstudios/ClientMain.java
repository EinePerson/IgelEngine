package de.igelstudios;

import de.igelstudios.igelengine.client.lang.ClientConfig;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.io.EngineSettings;
import de.igelstudios.igelengine.common.startup.EngineInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Client");
    private static ClientMain instance;

    public static synchronized ClientMain getInstance() {
        return instance;
    }

    public static void main(String args[]) {
        new ClientMain().start();
    }

    public ClientEngine getEngine() {
        return engine;
    }

    private ClientEngine engine;
    private ClientConfig config;
    private EngineInitializer main;
    private EngineSettings settings;

    public ClientMain(){
        this.settings = EngineSettings.parser("info.json").read();
        try {
            this.main = (EngineInitializer) Class.forName(this.settings.getMain()).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException|java.lang.reflect.InvocationTargetException|InstantiationException|IllegalAccessException|NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The Main class specified in info.json has to implement EngineInitializer");
        }
        this.main.onInitialize();
        instance = this;
        config = new ClientConfig();
        engine = new ClientEngine(main);
    }

    public void start(){
        engine.start();
    }
}
