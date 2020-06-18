package com.liyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MyNettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪时会触发该方法, 这里触发后将给服务器发送消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StudentPO.Student liyi = StudentPO.Student.newBuilder().setId(1).setName("liyi").build();

        //super.channelActive(ctx);
        //ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,Server",CharsetUtil.UTF_8));
        ctx.writeAndFlush(liyi);
    }

    /**
     * 当通道有读事件就会触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        System.out.println("服务器发送的消息是 : " + ((ByteBuf)msg).toString(CharsetUtil.UTF_8));
    }
}
