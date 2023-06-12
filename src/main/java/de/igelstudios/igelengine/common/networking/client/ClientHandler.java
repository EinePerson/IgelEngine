package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.igelengine.common.networking.PacketByteBuf;

@FunctionalInterface
public interface ClientHandler {
    void receive(Client client, PacketByteBuf buf);
}
