package de.igelstudios;

import de.igelstudios.igelengine.client.ClientConfig;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.common.io.EngineSettings;
import de.igelstudios.igelengine.common.networking.client.Client;
import de.igelstudios.igelengine.common.networking.client.ClientConnectListener;
import de.igelstudios.igelengine.common.startup.EngineInitializer;

import java.lang.reflect.InvocationTargetException;

public class ClientMain {
    //public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Client");
    private static ClientMain instance;

    public static synchronized ClientMain getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        new ClientMain().start();
    }

    public ClientEngine getEngine() {
        return engine;
    }

    private ClientEngine engine;
    private ClientConfig config;
    private EngineInitializer main;
    private EngineSettings settings;
    private ClientConnectListener listener;


    /**
     * This initializes the Engine for the Client with the settings from the info.json file
     */
    public ClientMain(){
        this.settings = EngineSettings.parser("info.json").read();
        try {
            this.main = (EngineInitializer) Class.forName(this.settings.getMain()).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException|java.lang.reflect.InvocationTargetException|InstantiationException|IllegalAccessException|NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The Main class specified in info.json has to implement EngineInitializer");
        }
        if(this.settings.getClientEventListener() != null && !this.settings.getClientEventListener().equals("")) {
            try {
                Client.addConnectionListener((ClientConnectListener) Class.forName(this.settings.getClientEventListener()).getConstructor(new Class[0]).newInstance(new Object[0]));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException |
                     ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (ClassCastException e) {
                throw new RuntimeException("The class specified as client_event_listener in info.json has to implement ServerInitializer");
            }
        }
        instance = this;
        config = new ClientConfig();
        engine = new ClientEngine(main,settings.getName());
    }

    public void start(){
        engine.start();
    }

    public ClientConnectListener getListener() {
        return listener;
    }

    public EngineSettings getSettings() {
        return settings;
    }
}
