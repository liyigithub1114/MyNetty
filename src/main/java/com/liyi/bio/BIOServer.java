package com.liyi.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用telnet 连接
 * telnet 127.0.0.1 6666
 * ctrl + ]
 * send data
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("服务器启动...");

        while(true){
            System.out.println("等待连接...");
            Socket accept = serverSocket.accept();
            System.out.println("接收到一个客户端");
            executorService.execute(() -> {
                handler(accept);
            });
        }
    }

    public static void handler(Socket socket){
        System.out.println("current thread id is : " + Thread.currentThread().getId() + " current thread name is :" + Thread.currentThread().getName() );

        InputStream inputStream = null;
        try {
            byte[] bytes = new byte[1024];
            inputStream = socket.getInputStream();
            while(true){
                System.out.println("current thread id is : " + Thread.currentThread().getId() + " current thread name is :" + Thread.currentThread().getName() );
                System.out.println("等待read...");
                int read = inputStream.read(bytes);
                if(read != -1){
                    System.out.println(new String(bytes, 0 ,read));
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
