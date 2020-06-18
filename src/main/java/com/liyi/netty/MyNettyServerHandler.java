package com.liyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MyNettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 接受客户端发送的消息
     * @param ctx 上下文，含有pipeline
     * @param msg 客户端发送的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
//        ByteBuf byteBuf = (ByteBuf)msg;
//        System.out.println("客户端发送的消息是: " + byteBuf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址: " + ctx.channel().remoteAddress());
        StudentPO.Student liyi = (StudentPO.Student)msg;
        System.out.println(liyi.getId() + "   " + liyi.getName());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,Client", CharsetUtil.UTF_8));
    }

    /**
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
