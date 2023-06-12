package de.igelstudios;

import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Client");
    private static ClientMain instance;

    public static synchronized ClientMain getInstance() {
        return instance;
    }

    public static void main(String args[]) throws InterruptedException {
        //new Test().run();
        new ClientMain();
        //Client client = new Client("127.0.0.1",Client.DEFAULT_PORT);
        //client.start();
    }

    public ClientEngine getEngine() {
        return engine;
    }

    private ClientEngine engine;

    public ClientMain(){
        engine = new ClientEngine();

        instance = this;

        engine.run();
    }
}
