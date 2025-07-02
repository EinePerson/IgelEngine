package de.igelstudios.igelengine.common.networking.client;

public interface ClientConnectListener {

    void playerConnect();

    void playerDisConnect();

    void connectionFailed();
}
