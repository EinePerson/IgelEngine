package de.igelstudios.igelengine.server;

import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.common.networking.client.Client;
import de.igelstudios.igelengine.common.networking.client.ClientNet;
import de.igelstudios.igelengine.common.networking.server.Server;
import de.igelstudios.igelengine.common.networking.server.ServerMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerEngine extends Engine implements Runnable {
    private Map<UUID, ClientNet> players;
    private Server server;
    private ErrorHandler handler;

    public ServerEngine(ErrorHandler handler){
        this(Client.DEFAULT_PORT,handler);
    }

    public ServerEngine(int port,ErrorHandler handler){
        this.handler = handler;
        players = new HashMap<>();
        server = new Server(port,players,handler);
        addTickable(server);
    }

    @Override
    public void start() {
        server.start();
        super.start();
    }

    @Override
    public void tick() {

    }

    public ClientNet get(UUID uuid){
        return players.get(uuid);
    }

    public void add(ClientNet net){
        players.put(net.getUUID(),net);
    }

    public Map<UUID, ClientNet> getPlayers() {
        return new HashMap<>(players);
    }
}
