package com.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    //GlobalEventExecutor.INSTANCE 是全局的事件执行器,是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //有客户端连接时调用 提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel  = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress() + "  上线了 " + sdf.format(new Date()) + "\n");
        channelGroup.add(channel);
        System.out.println(channel.remoteAddress() + "上线了"  + "\n");
    }

    //表示 channel 处于不活动状态 , 提示离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将客户端离开的信息推送给当前在线的客户
        channel.writeAndFlush("[客户端]"+channel.remoteAddress() + "  下线了 " + sdf.format(new Date()) + "\n");
        System.out.println(channel.remoteAddress()+"  下线了 " +"\n");
        System.out.println("channalGroup size = " +channelGroup.size());
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch->{
            if(channel != ch){
                ch.writeAndFlush("[客户端]"+channel.remoteAddress()+" 发送了消息: " + msg +"\n");
            }else {
                ch.writeAndFlush("[ 自己 ] 发送了消息: "+ msg +"\n" );
            }
        });

    }



}
