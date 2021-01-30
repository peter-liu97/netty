package com.netty.reconnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ReconnectClientHandlerAdapter extends SimpleChannelInboundHandler<String> {
    private final ReconnectClient client = new ReconnectClient();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        client.connect();
    }
}
