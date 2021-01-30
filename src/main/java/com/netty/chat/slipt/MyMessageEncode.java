package com.netty.chat.slipt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 转码器
 * @author liushimin
 */
public class MyMessageEncode extends MessageToByteEncoder<MyMessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessageProtocol msg, ByteBuf out) throws Exception {

        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());

    }
}
