package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.networking.PacketByteBuf;
import de.igelstudios.igelengine.common.networking.client.ClientNet;

@FunctionalInterface
public interface ServerHandler {
    void receive(ClientNet sender, PacketByteBuf buf);
}
