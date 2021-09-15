package com.starter.irpc.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.domain.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 19:51
 **/
public class RpcRequestDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() <4){
            return;
        }
        int len = byteBuf.readInt();
        if (byteBuf.readableBytes()<len){
            return;
        }
        final byte[] bytes = new byte[len];
        byteBuf.getBytes(byteBuf.readerIndex(),bytes,0,len);
        list.add(JSON.parseObject(new String(bytes),RpcRequest.class));
    }
}
