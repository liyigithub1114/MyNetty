package com.liyi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class MyNettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup executors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(executors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast("encoder",new ProtobufEncoder())
                                .addLast(new MyNettyClientHandler());
                    }
                });

        ChannelFuture sync = bootstrap.connect("127.0.0.1", 7777).sync();
        sync.channel().closeFuture().sync();
    }
}
