package com.liyi.rpc;

import com.liyi.rpc.service.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        //定义协议 HelloService#hello#你好
        String protocol = msg.toString();

        System.out.println("Server 接受的原生 msg: " + msg.toString());
        if(protocol.startsWith("HelloService#hello#")){
            String realMsg = protocol.substring(protocol.lastIndexOf("#")  + 1);
            String res = new HelloServiceImpl().hello(realMsg);
            ctx.writeAndFlush(res);
        }
    }
}
