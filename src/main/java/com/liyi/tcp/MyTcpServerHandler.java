package com.liyi.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyTcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, CharsetUtil.UTF_8);

        System.out.println("接受到client的消息 : " + ++count);
        System.out.println("消息: " + message);

        //回复
        ByteBuf byteBuf = Unpooled.copiedBuffer(UUID.randomUUID().toString(),CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }
}
