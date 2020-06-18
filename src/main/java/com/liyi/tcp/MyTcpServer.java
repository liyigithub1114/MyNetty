package com.liyi.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.List;

public class MyTcpServer {

    public void run(List<ChannelHandler> channelHandlers, Integer port){
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
                            pipeline.addLast(new MyTcpServerHandler());
                        }
                    });

            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MyTcpServer().run(null,7337);
    }
}
