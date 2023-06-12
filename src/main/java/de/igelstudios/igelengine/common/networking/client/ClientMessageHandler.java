package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.igelengine.common.networking.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketException;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Package> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) throws Exception {
        Client.handle(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause instanceof SocketException){
            System.out.println("Disconnected from Server");
            Client.instance.stop();
        }
        else cause.printStackTrace();

    }
}
