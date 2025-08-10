package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.ClientMain;
import de.igelstudios.igelengine.common.networking.*;
import de.igelstudios.igelengine.common.networking.Package;
import de.igelstudios.igelengine.common.networking.server.ConnectionListener;
import de.igelstudios.igelengine.common.networking.server.ServerHandler;
import de.igelstudios.igelengine.common.util.Tickable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.*;

/**
 * This handles the client code
 * {@link #registerServer2ClientHandler(String, ClientHandler)} may be used to register a handler class with the specific id,the id MUST be specified when sending data to this handler
 * {@link #send2Server(String, PacketByteBuf)} may be used to send data to the Server
 * @see ClientHandler
 * @see de.igelstudios.igelengine.common.networking.server.Server
 */
public class Client extends Thread implements Tickable {
    private static List<ClientConnectListener> connectionListeners = new ArrayList<>();
    static Client instance;
    private static Map<String, ClientHandler> clientHandlers = new HashMap<>();

    private static List<Package> queueHandling = Collections.synchronizedList(new ArrayList<>());
    public static void handle(Package p){
        queueHandling.add(p);
    }

    private static void handleSelf(Package p){
        clientHandlers.get(p.id()).receive(instance,p.buf());
    }

    /**
     * This registers a handler for a type of message send over the network to the client
     * @param id the name of the packet
     * @param handler the specific handler
     */
    public static void registerServer2ClientHandler(String id, ClientHandler handler){
        clientHandlers.putIfAbsent(id,handler);
    }

    private static Map<String,PacketByteBuf> queuedPackets = new HashMap<>();

    /**
     * this method sends a message to the server
     * @param id the name of the packet
     * @param buf the buffer containing the message data
     */
    public synchronized static void send2Server(String id, PacketByteBuf buf){
        if(instance.channel == null)queuedPackets.put(id,buf);
        else instance.channel.writeAndFlush(new Package(id,buf));
    }

    private Channel channel;
    public static final int DEFAULT_PORT = 7777;
    private String host;
    private int port;
    private EventLoopGroup workGroup;
    private ErrorHandler errorHandler;

    /**
     * This constructs a new client, it is only connected to the address when it is started
     * @param host the host address
     * @param port the port of the server
     * @param handler the error handler for networking errors
     * @see Client#Client(String[], ErrorHandler) Client
     * @see Client#Client(String, ErrorHandler)  Client
     */
    public Client(String host,int port,ErrorHandler handler){
        this.host = host;
        this.port = port;
        workGroup = new NioEventLoopGroup();
        instance = this;
        errorHandler = handler;
        ClientMain.getInstance().getEngine().addTickable(this);
    }

    /**
     * This constructs a new client, it is only connected to the address when it is started
     * @param v an array of the address and optionally the port,if non is specified the default port is used
     * @param handler the error handler for networking errors
     * @see Client#Client(String, int, ErrorHandler)  Client
     * @see Client#Client(String, ErrorHandler)  Client
     */
    public Client(String[] v,ErrorHandler handler){
        this(v[0],v.length == 2 ? Integer.parseInt(v[1]):DEFAULT_PORT,handler);
    }

    /**
     * This constructs a new client, it is only connected to the address when it is started
     * @param host the host string which may include a port seperated by ':'
     * @param handler the error handler for networking errors
     * @see Client#Client(String, int, ErrorHandler)  Client
     * @see Client#Client(String[], ErrorHandler) Client
     */
    public Client(String host,ErrorHandler handler){
        this(host.split(":"),handler);
    }

    @Override
    public void run(){
        try {
            Bootstrap bootstrap = new Bootstrap().group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(createChannel());
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            queuedPackets.forEach(Client::send2Server);
            queuedPackets.clear();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            if(e instanceof ConnectException){
                connectionListeners.forEach(ClientConnectListener::connectionFailed);
            }
        }
    }

    /**
     * Disconnects and stops the client
     */
    public void stopClient(){
        errorHandler.handle(new ConnectException("Disconnected from " + host + ":" + port));
        workGroup.shutdownGracefully();
    }

    public static void disconnect(){
        instance.stopClient();
    }

    public ChannelInitializer<SocketChannel> createChannel(){
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("encoder", new Encoder());
                pipeline.addLast("decoder",new Decoder());
                pipeline.addLast(new ClientMessageHandler(errorHandler));
            }
        };
    }

    @Override
    public void tick() {
        queueHandling.forEach(Client::handleSelf);
        queueHandling.clear();
    }

    public static void addConnectionListener(ClientConnectListener connectionListener){
        connectionListeners.add(connectionListener);
    }

    public static void removeConnectionListener(ClientConnectListener connectionListener){
        connectionListeners.remove(connectionListener);
    }

    public static List<ClientConnectListener> getConnectionListeners() {
        return connectionListeners;
    }
}
