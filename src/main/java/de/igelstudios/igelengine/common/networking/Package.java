package de.igelstudios.igelengine.common.networking;

/**
 * This is a bundle of data that may be sent over the network
 * @param id the id that the receiver expects and that is associated with the appropriate handler of the package data
 * @param buf the actual data to be sent
 */
public record Package(String id,PacketByteBuf buf) {

    public Package(String name){
        this(name,PacketByteBuf.create());
    }

    public void write(byte b){
        buf.writeByte(b);
    }
}
