package com.liyi.nio.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class MyNIOServer {

    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(7777));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //创建Selector
        Selector selector = Selector.open();

        //注册channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            //该方法阻塞，1000毫秒后返回
            if (selector.select(1000) == 0) {
                System.out.println("没有事件发生 ");
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            //对发生的事件进行迭代
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(next.isReadable()){
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer attachment = (ByteBuffer) next.attachment();
                    channel.read(attachment);
                    System.out.println("从客户端接受到数据 ： "+ new String(attachment.array()));
                }
                //移除注册， 防止多线程下的重复消费
                iterator.remove();
            }
        }

    }
}
