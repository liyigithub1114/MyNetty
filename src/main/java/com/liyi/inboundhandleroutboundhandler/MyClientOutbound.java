package com.liyi.inboundhandleroutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClientOutbound {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(boss)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("encoder", new MyClientEncoder())
                                .addLast("decoder", new MyServerDecoder())
                                .addLast("handler", new MyClientOutHandler());
                    }
                });

        ChannelFuture sync = bootstrap.connect("127.0.0.1", 7379).sync();
        sync.channel().closeFuture().sync();
    }
}
