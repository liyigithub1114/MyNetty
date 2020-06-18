package com.liyi.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

public class MyTcpClient {

    public void run(List<ChannelHandler> channelHandlers,String host, Integer port){
        EventLoopGroup boss = null;
        try{
            boss = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(boss)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new MyTcpClientHandler());
                        }
                    });

            ChannelFuture sync = bootstrap.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MyTcpClient().run(null,"127.0.0.1",7337);
    }
}
