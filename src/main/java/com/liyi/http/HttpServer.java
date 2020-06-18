package com.liyi.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = null;
        EventLoopGroup worker = null;
        try{
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyHttpChannelHandler());

            ChannelFuture sync = bootstrap.bind(5379).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println("出错了...");
        }finally {

        }

    }
}
