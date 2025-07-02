package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.igelengine.common.networking.PacketByteBuf;
import de.igelstudios.igelengine.common.networking.server.Server;

/**
 * This interface may be employed to register and receive data send to the Server from Clients
 * @see Client
 */
@FunctionalInterface
public interface ClientHandler {
    void receive(Client client, PacketByteBuf buf);
}
