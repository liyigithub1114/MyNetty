package com.liyi.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush("[客户端] " + channel.remoteAddress() + " 加入群聊");
        group.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.writeAndFlush("[客户端] " + channel.remoteAddress() + " 退出群聊");
        //不用从group删除, 此事件会触发group的remove
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        group.forEach(ch -> {
            if(ch != channel){
                ch.writeAndFlush("[客户] " + channel.remoteAddress() + " 发送了 " + msg);
            }else{
                ch.writeAndFlush("[自己] " + " 发送了 " + msg);
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        group.writeAndFlush("[客户] " + ctx.channel().remoteAddress() + " 上线了~");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        group.writeAndFlush("[客户] " + ctx.channel().remoteAddress() + " 离线了~");

    }
}
