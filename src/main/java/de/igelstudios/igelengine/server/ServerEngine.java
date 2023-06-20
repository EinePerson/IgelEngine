package de.igelstudios.igelengine.server;

import de.igelstudios.igelengine.common.Engine;
import de.igelstudios.igelengine.common.networking.client.ClientNet;
import de.igelstudios.igelengine.common.networking.server.Server;
import de.igelstudios.igelengine.common.networking.server.ServerMessageHandler;

import java.util.Map;
import java.util.UUID;

public class ServerEngine extends Engine {
    private Map<UUID, ClientNet> players;
    private Server server;

    public ServerEngine(){
        server = new Server(players);
    }

    public ServerEngine(int port){
        server = new Server(port,players);
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
}
