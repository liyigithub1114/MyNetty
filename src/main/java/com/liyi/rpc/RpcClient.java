package com.liyi.rpc;

import com.liyi.rpc.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcClient {

    private final String protocolName = "HelloService#hello#";

    private RpcClientHandler clientHandler;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public Object getBean(Class<?> serviceClass){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass},(proxy, method, args)->{

            if(clientHandler == null){
                init();
            }

            clientHandler.setParam(protocolName + args[0]);

            return executorService.submit(clientHandler).get();
        });
    }

    public void init(){
        clientHandler = new RpcClientHandler();

        EventLoopGroup boss = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(boss)
                .channel(NioSocketChannel.class)
                //.option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(clientHandler);
                    }
                });

        try {
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 20880).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        HelloService helloService = (HelloService)rpcClient.getBean(HelloService.class);
        String liyi = helloService.hello("liyi");
        System.out.println(liyi);
    }


}
