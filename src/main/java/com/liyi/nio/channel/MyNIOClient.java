package com.liyi.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MyNIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7777);


        if (!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("client连接失败, 旦可以进行其他操作");
            }
        }

        ByteBuffer wrap = ByteBuffer.wrap("Hello,Netty".getBytes());
        socketChannel.write(wrap);
        System.in.read();
    }
}
