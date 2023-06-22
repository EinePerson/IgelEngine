package de.igelstudios;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.server.ServerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Server");

    private static ServerMain instance;
    private final ErrorHandler handler;

    public static void main(String[] args) {
        new ServerMain(Throwable::printStackTrace);
    }

    public  static synchronized ServerMain getInstance(){
        return instance;
    }

    private ServerEngine engine;

    public ServerMain(ErrorHandler handler){
        instance = this;
        this.handler = handler;
        engine = new ServerEngine(handler);
    }

    public void start(){
        engine.start();
    }

    public ServerEngine getEngine() {
        return engine;
    }
}