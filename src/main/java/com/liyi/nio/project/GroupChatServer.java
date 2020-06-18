package com.liyi.nio.project;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    private final int port = 7777;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public GroupChatServer() {
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            System.out.println("GroupChatServer初始化失败");
        }
    }

    //监听来自客户端的请求
    public void listener() {
        try {
            while (true) {
                if (this.selector.select() != 0) {//有事件发生
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        if (next.isAcceptable()) {
                            SocketChannel accept = this.serverSocketChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);
                            System.out.println(accept.getRemoteAddress() + "上线");
                        }
                        if (next.isReadable()) {
                            readFromClient(next);
                        }
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {

        } finally {

        }
    }

    public void readFromClient(SelectionKey selectionKey){
        SocketChannel socketChannel = null;
        try{
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            if(read > 0){
                String msg = new String(byteBuffer.array());
                System.out.println("读取到来自 " + socketChannel.getRemoteAddress() + " 的消息， 转发至其余客户端...");
                writeFromServer(msg, socketChannel);
            }
        }catch (Exception e){
            try {
                System.out.println(socketChannel.getRemoteAddress() + " 下线");
                selectionKey.cancel();//取消注册
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }finally {

        }
    }

    public void writeFromServer(String msg, SocketChannel self){
        try{
            Set<SelectionKey> keys = selector.keys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()){
                SelectionKey next = iterator.next();
                SelectableChannel channel = next.channel();

                if((channel instanceof SocketChannel) && (channel != self)){
                    ((SocketChannel) channel).write(ByteBuffer.wrap(msg.getBytes()));
                }
            }
        }catch (Exception e){

        }finally {

        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listener();
    }
}
