package de.igelstudios;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.io.EngineSettings;
import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.common.networking.client.ClientNet;
import de.igelstudios.igelengine.common.util.PlayerFactory;
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
    private EngineSettings settings;

    public ServerMain(ErrorHandler handler){
        this.settings = EngineSettings.parser("info.json").read();
        try {
            PlayerFactory.setPlayerClass((Class<? extends ClientNet>) Class.forName(settings.getPlayer()),false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (ClassCastException e){
            throw new RuntimeException("The class specified as player in info.json has to implement ClientNet");
        }
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