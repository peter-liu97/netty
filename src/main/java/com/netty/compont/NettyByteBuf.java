package com.netty.compont;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf {
    public static void main(String[] args) {
        //创建ByteBuf对象,该对象内部包含一个字节数组byte[10]
        //通过readerIndex和writerIndex和capacity,将buffer分为三个区域
        //已经读取的区域[0,readerIndex)
        //可读取的区域:[readerIndex, writerIndex)
        //可写的区域:[writerIndex,capacity)
        ByteBuf buffer = Unpooled.buffer(10);
        System.out.println("buffer = " + buffer);
        for (int i = 0; i < 8; i++) {
            buffer.writeByte(i);
        }
        System.out.println("buffer = " + buffer);
        for (int i = 0; i < 5; i++) {
            System.out.print(buffer.getByte(i));
        }
        System.out.println();
        System.out.println("getByte after buffer = " + buffer);
        for (int i = 0; i < 5; i++) {
            System.out.print(buffer.readByte());
        }
        System.out.println();
        System.out.println("readBytes after buffer = " + buffer);
    }
}
