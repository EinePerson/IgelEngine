package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.entity.Player;
import de.igelstudios.igelengine.common.networking.PacketByteBuf;

public class JoinHandler{
    public static void receive(Player sender, PacketByteBuf buf) {
        System.out.println(buf.readString());
    }
}
