package com.liyi.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过创建FileChannel输出流，写一个文件到当前目录下
 */
public class MyFileChannel {

    private static String dir = System.getProperty("user.dir") + "\\file";

    public static void main(String[] args) throws IOException {
        //write();
        //read();
        //copy();
        copyByTransferFrom();
    }

    public static void write() throws IOException{
        String info = "hello, netty!";
        FileOutputStream fileOutputStream = new FileOutputStream(dir + "\\FileChannel.txt");

        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(info.getBytes());
        byteBuffer.flip();

        channel.write(byteBuffer);
        channel.close();
    }

    public static void read() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(dir + "\\FileChannel.txt");

        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        channel.close();
    }

    public static void copy() throws IOException {
        FileInputStream inputStream = new FileInputStream(dir + "\\FileChannel.txt");
        FileOutputStream outputStream = new FileOutputStream(dir + "\\FileChannelCopy.txt");

        ByteBuffer byteBuffer = ByteBuffer.allocate(1);

        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();
        //1024 可能一次读不完，边读边写
        while(true){

            //一定要操作的一步，reset byteBuffer缓冲
            byteBuffer.clear();
            int read = inputStreamChannel.read(byteBuffer);
            if(read == -1){
                break;
            }
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }

        outputStream.close();
        inputStream.close();
    }

    public static void copyByTransferFrom() throws IOException{
        FileInputStream inputStream = new FileInputStream(dir + "\\FileChannel.txt");
        FileOutputStream outputStream = new FileOutputStream(dir + "\\FileChannelCopyByTransferFrom.txt");

        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();

        outputStreamChannel.transferFrom(inputStreamChannel, 0 ,inputStreamChannel.size());

        outputStreamChannel.close();
        inputStreamChannel.close();
        outputStream.close();
        inputStream.close();
    }
}
