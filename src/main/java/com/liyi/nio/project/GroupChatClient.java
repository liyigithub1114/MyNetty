package com.liyi.nio.project;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";

    private final int PORT = 7777;

    private SocketChannel socketChannel;

    private Selector selector;

    private String userName;

    public GroupChatClient(){
        try{
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
            this.socketChannel.configureBlocking(false);
            this.socketChannel.register(selector, SelectionKey.OP_READ);
            this.userName = this.socketChannel.getLocalAddress().toString().substring(1);
        }catch (Exception e){

        }
    }

    public void send2Server(String info){
        try{
            info = this.userName + "说: " + info;
            this.socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception e){

        }
    }

    public void readFromServer(){
        try{
            //while(true){
                if(this.selector.select() != 0){
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext()){
                        SelectionKey next = iterator.next();
                        if(next.isReadable()){
                            SocketChannel channel = (SocketChannel)next.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int read = channel.read(byteBuffer);
                            if(read > 0){
                                String msg = new String(byteBuffer.array());
                                System.out.println(msg);
                            }
                        }
                        iterator.remove();
                    }
                }
            //}
        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(() -> {
            while(true){
                try{
                    groupChatClient.readFromServer();
                    TimeUnit.SECONDS.sleep(3L);
                }catch (Exception e){

                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupChatClient.send2Server(s);
        }
    }
}
