package com.liyi.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyChatServer {

    private int port = 5379;

    public void run(){

        EventLoopGroup boss = null;
        EventLoopGroup worker = null;

        try{
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("MyDecoder",new StringDecoder())
                                    .addLast("MyEncoder", new StringEncoder())
                                    .addLast("MyServerHandler", new NettyChatServerHandler());
                        }
                    });

            ChannelFuture sync = bootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();

        }catch (Exception e){

        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyChatServer().run();
    }
}
