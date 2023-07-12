package de.igelstudios.igelengine.server;

import de.igelstudios.igelengine.common.io.Config;
import de.igelstudios.igelengine.common.networking.client.Client;

public class ServerConfig {
    private static Config config;
    private static int port;

    public static Config get(){
        if(config == null)create();
        return config;
    }

    public static void create(){
        config = new Config("server");
        port = (int) config.get("port", Client.DEFAULT_PORT);
    }

    public static int getPort() {
        if(config == null)create();
        return port;
    }
}
