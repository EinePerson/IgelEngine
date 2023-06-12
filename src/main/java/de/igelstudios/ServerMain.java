package de.igelstudios;

import de.igelstudios.igelengine.client.Window;
import de.igelstudios.igelengine.common.networking.server.JoinHandler;
import de.igelstudios.igelengine.common.networking.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(Window.TITLE + ":Server");

    private static ServerMain instance;

    public static void main(String[] args) {
        new ServerMain().start();
    }

    public  static synchronized ServerMain getInstance(){
        return instance;
    }

    private Server server;

    public ServerMain(){
        instance = this;
        server = new Server();
        Server.registerClient2ServerHandler("Login", JoinHandler::receive);
    }

    public void start(){
        server.start();
    }
}