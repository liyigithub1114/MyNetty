package com.liyi.inboundhandleroutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerInHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("接受到服来在Client的Long : " + msg);
        ctx.channel().writeAndFlush(987654L);
    }
}
