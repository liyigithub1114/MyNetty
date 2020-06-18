package com.liyi.nio.channel;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Buffer的分散与聚集
 */
public class MyScatteringAndGathering {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(7000));

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //定义可接受最大字节， 对应buffers中所有元素累加和
        int maxReceiveByte = 8;
        SocketChannel accept = serverSocketChannel.accept();

        while (true) {
            long read = 0;

            while (read < maxReceiveByte) {
                long read1 = accept.read(byteBuffers);
                if(read1 == -1){
                    break;
                }
                read += read1;
                System.out.println("读取到: " + read);
                Arrays.asList(byteBuffers)
                        .stream()
                        .map(buffer -> "buffer position = " + buffer.position() + " buffer limit = " + buffer.limit())
                        .forEach(System.out::println);
            }


            Arrays.asList(byteBuffers).stream().forEach(buffer -> buffer.flip());

            long write = 0;
            while(write < maxReceiveByte){
                long write1 = accept.write(byteBuffers);
                if(write1 == -1){
                    break;
                }
                write += write1;
            }

            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
        }
    }
}
