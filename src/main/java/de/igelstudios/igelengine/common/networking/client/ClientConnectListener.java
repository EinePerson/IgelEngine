package de.igelstudios.igelengine.common.networking.client;

/**
 * This listener may be employed for code acting as server to receive updates for when the player connects and disconnects or the tried connection does not connect to handle those events accordingly
 * @see de.igelstudios.igelengine.common.networking.server.ConnectionListener
 */
public interface ClientConnectListener {

    void playerConnect();

    void playerDisConnect();

    void connectionFailed();
}
