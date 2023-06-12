package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.entity.Player;
import de.igelstudios.igelengine.common.networking.PacketByteBuf;

@FunctionalInterface
public interface ServerHandler {
    void receive(Player sender, PacketByteBuf buf);
}
