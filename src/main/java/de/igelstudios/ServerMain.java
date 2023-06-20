package de.igelstudios;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.networking.server.Server;
import de.igelstudios.igelengine.server.ServerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Server");

    private static ServerMain instance;

    public static void main(String[] args) {
        new ServerMain();
    }

    public  static synchronized ServerMain getInstance(){
        return instance;
    }

    private ServerEngine engine;

    public ServerMain(){
        instance = this;
        engine = new ServerEngine();
    }

    public void start(){
        engine.start();
    }

    public ServerEngine getEngine() {
        return engine;
    }
}