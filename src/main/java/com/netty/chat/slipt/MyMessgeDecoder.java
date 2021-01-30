package com.netty.chat.slipt;

import com.netty.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * @author liushimin
 */
public class MyMessgeDecoder extends ByteToMessageDecoder {

    private int length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if(in.readableBytes() >= 4){
            //读取数据的长度
            if(length == 0){
                length = in.readInt();
            }

            if(in.readableBytes()<length){
                //当前可读数据不够继续等待；
                return;
            }

            byte[] bytes = new byte[length];
            if(in.readableBytes()>length){
                in.readBytes(bytes);
                MyMessageProtocol protocol = new MyMessageProtocol();
                protocol.setLen(length);
                protocol.setContent(bytes);
                out.add(protocol);
            }
            length = 0;
        }

    }
}
