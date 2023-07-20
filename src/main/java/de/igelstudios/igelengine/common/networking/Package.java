package de.igelstudios.igelengine.common.networking;

public record Package(String id,PacketByteBuf buf) {

    public Package(String name){
        this(name,PacketByteBuf.create());
    }

    public void write(byte b){
        buf.writeByte(b);
    }
}
