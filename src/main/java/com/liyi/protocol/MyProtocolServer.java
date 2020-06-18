package com.liyi.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class MyProtocolServer {

    public void run(){
        EventLoopGroup boss = null;
        EventLoopGroup worker = null;
        try{
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new MyProtocolDecoder());
                            pipeline.addLast(new MyProtocolEncoder());
                            pipeline.addLast(new MyProtocolServerHandler());
                        }
                    });

            ChannelFuture sync = serverBootstrap.bind(6666).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MyProtocolServer().run();
    }
}
