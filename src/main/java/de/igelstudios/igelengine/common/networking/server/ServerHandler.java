package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.networking.PacketByteBuf;
import de.igelstudios.igelengine.common.networking.client.ClientNet;

/**
 * This interface may be employed to register and receive data send to the Server from Clients
 * @see Server
 */
@FunctionalInterface
public interface ServerHandler {
    void receive(ClientNet sender, PacketByteBuf buf);
}
