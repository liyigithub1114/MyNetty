package com.liyi.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息: " + msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now() + " " + msg.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String s = ctx.channel().id().asLongText();//long唯一
        String s1 = ctx.channel().id().asShortText();//不唯一
        System.out.println("add long id = " + s);
        System.out.println("add short id = " + s1);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String s = ctx.channel().id().asLongText();//long唯一
        String s1 = ctx.channel().id().asShortText();//不唯一
        System.out.println("remove long id = " + s);
        System.out.println("remove short id = " + s1);
    }
}
