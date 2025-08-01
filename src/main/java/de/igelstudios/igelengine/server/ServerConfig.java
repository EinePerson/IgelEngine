package de.igelstudios.igelengine.server;

import de.igelstudios.igelengine.common.io.Config;
import de.igelstudios.igelengine.common.networking.client.Client;

/**
 * this is a theoretical unused class for Server exclusive settings
 */
public class ServerConfig {
    private static Config config;
    private static int port;

    public static Config get(){
        if(config == null)create();
        return config;
    }

    public static void create(){
        config = new Config("server");
        try {
            port = Integer.parseInt(config.getOrDefault("port", Client.DEFAULT_PORT).toString());
        }catch (NumberFormatException e){
            throw new RuntimeException("The port specified in server.properties has to be a number");
        }
    }

    public static int getPort() {
        if(config == null)create();
        return port;
    }
}
