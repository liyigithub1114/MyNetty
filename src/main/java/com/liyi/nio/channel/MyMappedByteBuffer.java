package com.liyi.nio.channel;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 不用操作系统拷贝，可以在非对内存进行拷贝，写入到文件操作由NIO完成
 * 可以认为是DirectMemory
 *
 * FileChannel可能不能即使显示，可以在文件真实目录打开
 */
public class MyMappedByteBuffer {

    private static String dir = System.getProperty("user.dir") + "\\file";

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(dir + "\\FileChannel.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 1.FileChannel.MapMode.READ_WRITE 读写模式
         * 2.起始位置
         * 3.最多可更改的位置 - 1
         * 即 0 - 4
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        randomAccessFile.close();
    }
}
