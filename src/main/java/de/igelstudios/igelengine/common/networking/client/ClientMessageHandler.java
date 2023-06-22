package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.common.networking.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.ConnectException;
import java.net.SocketException;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Package> {
    private ErrorHandler handler;

    public ClientMessageHandler(ErrorHandler handler){
        this.handler = handler;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) throws Exception {
        Client.handle(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        handler.handle(cause);
    }
}
