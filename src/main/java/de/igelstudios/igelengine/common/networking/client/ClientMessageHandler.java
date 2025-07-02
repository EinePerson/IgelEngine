package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.common.networking.Package;
import de.igelstudios.igelengine.common.networking.server.Server;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.ConnectException;
import java.net.SocketException;

/**
 * This is an internal class used as bridge for networking
 * @see Client
 */
public class ClientMessageHandler extends SimpleChannelInboundHandler<Package> {
    private ErrorHandler handler;

    public ClientMessageHandler(ErrorHandler handler){
        this.handler = handler;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) throws Exception {
        Client.handle(msg);
    }

    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Package){
            Client.handle((Package) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        handler.handle(cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Client.getConnectionListeners().forEach(ClientConnectListener::playerConnect);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Client.getConnectionListeners().forEach(ClientConnectListener::playerDisConnect);
    }
}
