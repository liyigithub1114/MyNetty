package com.liyi.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("MyHttpServerCodec",new HttpServerCodec())
                                //传输是以块的形式进行的
                                .addLast("MyChunkedWriteHandler",new ChunkedWriteHandler())
                                //传输是分段传输的
                                .addLast("MyHttpHttpObjectAggregator", new HttpObjectAggregator(8192))
                                //传输地址ws://localhost:port/hello
                                .addLast("MyWebSocketServerProtocolHandler",new WebSocketServerProtocolHandler("/hello"))
                                .addLast("MyWebSocketHandler",new MyWebSocketHandler());
                    }
                });

        ChannelFuture sync = bootstrap.bind(5379).sync();
        sync.channel().closeFuture().sync();
    }
}
