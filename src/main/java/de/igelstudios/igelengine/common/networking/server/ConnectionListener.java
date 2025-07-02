package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.networking.client.ClientNet;

/**
 * This listener may be employed for code acting as server to receive updates for when players connect and disconnect to handle those events accordingly
 * @see de.igelstudios.igelengine.common.networking.client.ClientConnectListener
 */
public interface ConnectionListener {

    void playerConnect(ClientNet player);

    void playerDisConnect(ClientNet player);
}
