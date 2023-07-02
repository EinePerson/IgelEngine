package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.networking.client.ClientNet;

public interface ConnectionListener {

    void playerConnect(ClientNet player);

    void playerDisConnect(ClientNet player);
}
