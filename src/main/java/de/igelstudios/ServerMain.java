package de.igelstudios;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.io.EngineSettings;
import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.common.networking.client.Client;
import de.igelstudios.igelengine.common.networking.client.ClientNet;
import de.igelstudios.igelengine.common.networking.server.ConnectionListener;
import de.igelstudios.igelengine.common.networking.server.Server;
import de.igelstudios.igelengine.common.startup.ServerInitializer;
import de.igelstudios.igelengine.common.util.PlayerFactory;
import de.igelstudios.igelengine.server.ServerConfig;
import de.igelstudios.igelengine.server.ServerEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class ServerMain {
    //public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Server");

    private static ServerMain instance;
    private final ErrorHandler handler;

    public static void main(String[] args) {
        new ServerMain(25).start();
    }
    public  static synchronized ServerMain getInstance(){
        return instance;
    }

    private ServerEngine engine;
    private EngineSettings settings;
    private ServerInitializer main;
    private ConnectionListener listener;

    /**
     * This initializes the Engine for the Server with the settings from the info.json file
     * @param port the port the server should be started on
     */
    public ServerMain(int port){
        this.settings = EngineSettings.parser("info.json").read();
        try {
            PlayerFactory.setPlayerClass((Class<? extends ClientNet>) Class.forName(settings.getPlayer()),false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (ClassCastException e){
            throw new RuntimeException("The class specified as player in info.json has to implement ClientNet");
        }

        try {
            this.main = (ServerInitializer) Class.forName(this.settings.getServerMain()).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch (ClassCastException e){
            throw new RuntimeException("The class specified as server_main in info.json has to implement ServerInitializer");
        }

        if(this.settings.getServerErrorHandler() != null && !this.settings.getServerErrorHandler().equals("")) {
            try {
                this.handler = (ErrorHandler) Class.forName(this.settings.getServerErrorHandler()).getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException |
                     ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (ClassCastException e) {
                throw new RuntimeException("The class specified as server_error_handler in info.json has to implement ServerInitializer");
            }
        }else this.handler = Throwable::printStackTrace;

        if(this.settings.getServerEventListener() != null && !this.settings.getServerEventListener().equals("")) {
            try {
                this.listener = (ConnectionListener) Class.forName(this.settings.getServerEventListener()).getConstructor(new Class[0]).newInstance(new Object[0]);
                Server.addConnectionListener(this.listener);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException |
                     ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (ClassCastException e) {
                throw new RuntimeException("The class specified as server_error_handler in info.json has to implement ServerInitializer");
            }
        }
        instance = this;
        engine = new ServerEngine(port,handler);
        this.main.onInitialize();
    }

    public void start(){
        engine.start();
    }

    public ServerEngine getEngine() {
        return engine;
    }

    /**
     * @return the Error handler for networking errors
     */
    public ErrorHandler getHandler() {
        return handler;
    }
}
