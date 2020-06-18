package com.liyi.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class RpcClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext ctx;

    private String param;

    private String result;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();
    }

    @Override
    public synchronized Object call() throws Exception {
        this.ctx.writeAndFlush(param);
        wait();
        return result;
    }

    public void setParam(String msg){
        this.param = msg;
    }
}
