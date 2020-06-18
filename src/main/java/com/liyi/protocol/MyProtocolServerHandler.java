package com.liyi.protocol;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyProtocolServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        String content = new String(msg.getContent(), CharsetUtil.UTF_8);

        System.out.println("服务器第 " + ++count + " 次接收完整包");
        System.out.println("服务器接收的消息长度 : " + len + "    内容 : " + content);

        System.out.println("\n\n\n\n");

        //回复
        String responseContent = UUID.randomUUID().toString();
        MessageProtocol message = new MessageProtocol();
        message.setLen(responseContent.getBytes().length);
        message.setContent(responseContent.getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(message);

    }
}
