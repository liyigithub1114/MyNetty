package com.liyi.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyProtocolDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MessageProtocol messageProtocol = new MessageProtocol();
        //获取长度
        int len = in.readInt();
        messageProtocol.setLen(len);
        //将内容写入byte[]
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        messageProtocol.setContent(bytes);


        //将message传给下一个handler
        out.add(messageProtocol);
    }
}
