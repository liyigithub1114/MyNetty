package com.liyi.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyTcpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 1; i < 11; i++){
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello Server " + i + "\n", CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String message = new String(bytes, CharsetUtil.UTF_8);

        System.out.println("客户端收到消息 : " + message);
        System.out.println("客户端收到消息数量： " + ++count);
    }
}
