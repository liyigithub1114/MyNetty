package com.liyi.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class MyChannelHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if(httpObject instanceof HttpRequest){
            System.out.println("msg： " + httpObject);

            HttpRequest request = (HttpRequest)httpObject;
            URI uri = new URI(request.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("不进行此次处理");
                return;
            }
        }
        System.out.println("收到来自客户端的请求: " + channelHandlerContext.channel().remoteAddress());

        ByteBuf buf = Unpooled.copiedBuffer("Hello, i'm server", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,buf);
        response.headers().set( HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        channelHandlerContext.writeAndFlush(response);
    }
}
