package com.liyi.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyProtocolClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        String content = new String(msg.getContent(),CharsetUtil.UTF_8);
        System.out.println("收到回复长度 : " + len + "    内容 : " + content);

        System.out.println("\n\n\n\n\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for(int i = 1; i < 4; i++){
            MessageProtocol messageProtocol = new MessageProtocol();
            String content = "Hello protocol";

            messageProtocol.setContent(content.getBytes(CharsetUtil.UTF_8));
            messageProtocol.setLen(content.getBytes().length);

            ctx.writeAndFlush(messageProtocol);
        }

    }
}
