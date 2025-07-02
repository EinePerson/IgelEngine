package de.igelstudios.igelengine.common.networking.server;

import de.igelstudios.igelengine.common.networking.*;
import de.igelstudios.igelengine.common.networking.Package;
import de.igelstudios.igelengine.common.networking.client.Client;
import de.igelstudios.igelengine.common.networking.client.ClientNet;
import de.igelstudios.igelengine.common.util.Tickable;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.util.*;

/**
 * This handles the server code
 * {@link #registerClient2ServerHandler(String, ServerHandler)} may be used to register a handler class with the specific id,the id MUST be specified when sending data to this handler
 * {@link #send2Client(ClientNet, String, PacketByteBuf)} may be used to send data to the Specified connected client
 * @see ServerHandler
 * @see Client
 */
public class Server extends Thread implements Tickable {
    private static Map<ClientNet,Package> queueHandling = new HashMap<>();
    private static List<ConnectionListener> connectionListeners = new ArrayList<>();
    private static final Map<String, ServerHandler> serverHandlers = new HashMap<>();

    /**
     * This registers a handler for a type of message send over the network to the server
     * @param id the name of the packet
     * @param handler the specific handler
     */
    public static void registerClient2ServerHandler(String id, ServerHandler handler){
        serverHandlers.putIfAbsent(id,handler);
    }

    public static void handle(ClientNet player, Package p){
        queueHandling.put(player,p);
    }

    private static void handleSelf(ClientNet player, Package p){
        serverHandlers.get(p.id()).receive(player,p.buf());
    }
    private static Server instance;

    /**
     * this method sends a message to the player
     * @param player the player the message should be sent to
     * @param id the name of the packet
     * @param buf the buffer containing the message data
     */
    public static void send2Client(ClientNet player, String id, PacketByteBuf buf){
        instance.handler.channels.find(instance.handler.get(player)).writeAndFlush(new Package(id,buf));
    }

    private final int port;
    private EventLoopGroup boosGroup;
    private EventLoopGroup workerGroup;
    private ServerMessageHandler handler;
    private ErrorHandler errorHandler;

    /**
     * Creates a new server and binds it to the default port
     * @param map a map that maps UUID to players, this is not copied, so it is directly changed on (dis)connects
     * @param handler the handler that handles errors occurring
     * @see Server#Server(int, Map, ErrorHandler)  Server
     */
    public Server(Map<UUID, ClientNet> map,ErrorHandler handler){
        this(Client.DEFAULT_PORT,map,handler);
    }

    /**
     * Creates a new server
     * @param port the port the server should bind on
     * @param map a map that maps UUID to players, this is not copied, so it is directly changed on (dis)connects
     * @param errorHandler the handler that handles errors occurring
     * @see Server#Server(Map, ErrorHandler)  Server
     */
    public Server(int port,Map<UUID, ClientNet> map,ErrorHandler errorHandler){
        this.port = port;
        this.boosGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.handler = new ServerMessageHandler(errorHandler);
        handler.setPlayers(map);
        this.errorHandler = errorHandler;
        instance = this;
    }

    @Override
    public void run(){
        try {
            System.out.println("Started Server on " + port);
            ServerBootstrap bootstrap = new ServerBootstrap().group(boosGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true).childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true);

            bootstrap.childHandler(createChannel());
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stopServer();
        }
    }

    /**
     * This stops the server gracefully
     */
    public void stopServer(){
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void terminate(){
        instance.stopServer();
    }

    public ChannelInitializer<SocketChannel> createChannel(){
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("encoder", new Encoder());
                pipeline.addLast("decoder",new Decoder());
                pipeline.addLast(new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors()),"handler",handler);
            }
        };
    }

    @Override
    public void tick() {
        queueHandling.forEach(Server::handleSelf);
        queueHandling.clear();
    }

    public static void addConnectionListener(ConnectionListener connectionListener){
        connectionListeners.add(connectionListener);
    }

    public static void removeConnectionListener(ConnectionListener connectionListener){
        connectionListeners.remove(connectionListener);
    }

    public static List<ConnectionListener> getConnectionListeners() {
        return connectionListeners;
    }
}
