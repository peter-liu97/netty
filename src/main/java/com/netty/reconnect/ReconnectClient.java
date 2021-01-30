package com.netty.reconnect;

import com.netty.chat.client.ChatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ReconnectClient {
    private final Bootstrap bootstrap = new  Bootstrap();
    private final EventLoopGroup group = new NioEventLoopGroup();
    public static void main(String[] args) {
        ReconnectClient client = new ReconnectClient();
        client.connect();
    }

    public ReconnectClient(){
        this.init();
    }
    public void init(){
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder",new StringDecoder());
                        pipeline.addLast("encoder",new StringEncoder());
                        pipeline.addLast(new ReconnectClientHandlerAdapter());
                    }
                });
    }
    public void connect(){
        ChannelFuture connect = bootstrap.connect("127.0.0.1", 9000);
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().eventLoop().schedule(()->{
                        System.out.println("重连服务器。。。");
                        try {
                            connect();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    },3000, TimeUnit.MILLISECONDS);
                }
            }
        });
    }

}
