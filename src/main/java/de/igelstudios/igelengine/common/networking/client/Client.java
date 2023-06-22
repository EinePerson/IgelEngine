package de.igelstudios.igelengine.common.networking.client;

import de.igelstudios.igelengine.common.networking.*;
import de.igelstudios.igelengine.common.networking.Package;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

public class Client extends Thread{
    static Client instance;
    private static Map<String, ClientHandler> clientHandlers = new HashMap<>();

    public static void handle(Package p){
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

    /**
     * this method sends a message to the server
     * @param id the name of the packet
     * @param buf the buffer containing the message data
     */
    public static void send2Server(String id, PacketByteBuf buf){
        instance.channel.writeAndFlush(new Package(id,buf));
    }

    private Channel channel;
    public static final int DEFAULT_PORT = 7777;
    private String host;
    private int port;
    private EventLoopGroup workGroup;
    private ErrorHandler errorHandler;

    public Client(String host,int port,ErrorHandler handler){
        this.host = host;
        this.port = port;
        workGroup = new NioEventLoopGroup();
        instance = this;
        errorHandler = handler;
    }

    public Client(String[] v,ErrorHandler handler){
        this(v[0],v.length == 2 ? Integer.parseInt(v[1]):DEFAULT_PORT,handler);
    }

    public Client(String host,ErrorHandler handler){
        this(host.split(":"),handler);
    }

    @Override
    public void run(){
        try {
            Bootstrap bootstrap = new Bootstrap().group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(createChannel());
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //channel = future.sync().channel();
            /*future.addListener((FutureListener<Void>) future1 -> {
                //if(!future1.isSuccess())System.out.println("A       A");
                //if(!future1.isSuccess())errorHandler.handle(future1.cause());
                errorHandler.handle(future1.cause());
            });*/
            channel = future.channel();
            PacketByteBuf buf = PacketByteBuf.create();
            buf.writeString("Amogus");
            send2Server("SUS",buf);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } /*finally {
            errorHandler.handle(new ConnectException("Disconnected from " + host + ":" + port));
            stopClient();
        }*/
    }

    public void stopClient(){
        errorHandler.handle(new ConnectException("Disconnected from " + host + ":" + port));
        workGroup.shutdownGracefully();
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
}
