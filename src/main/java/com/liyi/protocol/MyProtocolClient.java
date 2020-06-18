package com.liyi.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyProtocolClient {

    private int port = 6666;

    public void run(){
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
                            pipeline.addLast(new MyProtocolEncoder());
                            pipeline.addLast(new MyProtocolDecoder());
                            pipeline.addLast(new MyProtocolClientHandler());
                        }
                    });

            ChannelFuture sync = bootstrap.connect("127.0.0.1", 6666).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MyProtocolClient().run();
    }
}
