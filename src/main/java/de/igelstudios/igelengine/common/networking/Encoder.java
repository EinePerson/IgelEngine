package de.igelstudios.igelengine.common.networking;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Package> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Package msg, ByteBuf out) throws Exception {
        PacketByteBuf buf = new PacketByteBuf(out);
        buf.writeId(msg.id());
        buf.writeBytes(msg.buf().copy());
    }
}
