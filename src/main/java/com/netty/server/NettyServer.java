package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static void main(String[] args) {
        /**
         * 创建两个线程组 bossGroup 和  workerGroup , 含有的子线程NioEventLoop的个数默认为cpu核心数的2倍
         *
         * bossGroup 处理客户端连接请求, workerGroup处理客户端业务
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            //创建服务端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程来配置参数
            serverBootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务端的通道实现
                    //初始化服务器队列大小,服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接.
                    //多个客户端同事来的时候,服务端不能处理的请求放入队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {   //创建通道初始化对象,设置初始化参数

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //对workerGroup的SocketChannel设置处理器
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("netty server start..");

            //绑定一个端口并且同步, 设置一个ChannelFuture异步对象,通过isDone()等方法可以判断异步事件的执行情况
            //启动服务器(并绑定端口),bind是一部操作,sync方法是等待异步操作执行完毕.
            ChannelFuture cf = serverBootstrap.bind(9000).sync();
            //如果不加 sync 则需要监听服务端绑定的端口,监听服务端是否启动成功
           /*
            cf.addListener((future)->{
            //服务端启动成功做些事情 do something  嘿嘿嘿
                if (future.isSuccess()) {
                    System.out.println("监听9000端口成功");
                }else {
                    System.out.println("监听9000端口失败");
                }
            });
            */

           //等待服务端监听端口关闭, closeFuture是异步操作
           //通过sync方法同步等待通道关闭,这里内部调用的是object.wait() 方法
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
