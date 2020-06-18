package com.liyi.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class NettyChatClient {

    private int post = 5379;
    private String host = "127.0.0.1";

    public void run() {
        EventLoopGroup boss = null;
        try{
            boss = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(boss)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("MyDecoder",new StringDecoder())
                                    .addLast("MyEncoder",new StringEncoder())
                                    .addLast("MyNettyClientHandler", new NettyChatClientHandler());
                        }
                    });

            ChannelFuture sync = bootstrap.connect(host, post).sync();
            //sync.channel().closeFuture().sync();

            Channel channel = sync.channel();
            Scanner sc = new Scanner(System.in);
            while(sc.hasNextLine()){
                channel.writeAndFlush(sc.nextLine());
            }
        }catch (Exception e){

        }finally {
            boss.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new NettyChatClient().run();
    }

}
