package com.liyi.nio.buffer;

import java.nio.ByteBuffer;

public class MyBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        /**
         * position 当前位置
         * limit 限制可读写最大位置， 初始 = capacity 可更改
         * capacity 最大容量，不可更改，初始设置
         * mark 初始-1 , 可回滚的标记
         */

        //只可以读，不可以写
        ByteBuffer byteBuffer1 = byteBuffer.asReadOnlyBuffer();
    }
}
