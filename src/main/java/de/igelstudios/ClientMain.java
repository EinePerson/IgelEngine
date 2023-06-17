package de.igelstudios;

import de.igelstudios.game.config.ClientConfig;
import de.igelstudios.igelengine.client.ClientEngine;
import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.client.graphics.text.GLFont;
import de.igelstudios.igelengine.client.lang.Text;
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
    private ClientConfig config;
    private final GLFont font;

    public ClientMain(){
        font = new GLFont("C:/Windows/Fonts/Candaraz.ttf", 64);
        engine = new ClientEngine();

        instance = this;
        config = new ClientConfig();
        System.out.println(Text.translatable("a"));

        engine.start();
    }
}
