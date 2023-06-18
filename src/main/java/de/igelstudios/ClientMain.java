package de.igelstudios;

import de.igelstudios.game.config.ClientConfig;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.client.graphics.text.GLFontGen;
import de.igelstudios.igelengine.client.lang.Text;
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
    private ClientConfig config;
    private final GLFontGen font;

    public ClientMain(){
        font = new GLFontGen("C:/Windows/Fonts/Candaraz.ttf", 64);
        engine = new ClientEngine();

        instance = this;
        config = new ClientConfig();

        engine.start();
    }
}
