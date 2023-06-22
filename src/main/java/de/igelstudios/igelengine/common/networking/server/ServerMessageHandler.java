package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.networking.ErrorHandler;
import de.igelstudios.igelengine.common.networking.Package;
import de.igelstudios.igelengine.common.networking.client.ClientNet;
import de.igelstudios.igelengine.common.util.PlayerFactory;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@ChannelHandler.Sharable
public class ServerMessageHandler extends SimpleChannelInboundHandler<Package> {
    private Map<ChannelId, UUID> playerIds;
    private Map<UUID, ClientNet> players;
    ChannelGroup channels;
    private ErrorHandler handler;

    ChannelId get(ClientNet player){
        UUID id = null;
        for (UUID uuid : players.keySet()) {
            if(players.get(uuid).equals(player)){
                id = uuid;
                break;
            }
        }
        for (ChannelId channelId : playerIds.keySet()) {
            if(playerIds.get(channelId).equals(id))return channelId;
        }
        return null;
    }

    public ServerMessageHandler(ErrorHandler handler){
        playerIds = new HashMap<>();
        players = new HashMap<>();
        this.channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) throws Exception {
        Server.handle(players.get(ctx.channel().id()),msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("User connected");
        channels.add(ctx.channel());
        UUID uuid = UUID.randomUUID();
        playerIds.put(ctx.channel().id(),uuid);
        players.put(uuid, PlayerFactory.get(uuid));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("User disconnected");
        channels.remove(ctx.channel());
        UUID uuid = playerIds.get(ctx.channel().id());
        playerIds.remove(ctx.channel().id());
        players.remove(uuid);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        handler.handle(cause);
    }

    public void setPlayers(Map<UUID, ClientNet> players) {
        this.players = players;
    }
}
