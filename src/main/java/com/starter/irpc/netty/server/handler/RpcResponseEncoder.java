package com.starter.irpc.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.domain.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-08-11 21:46
 **/
public class RpcResponseEncoder extends MessageToByteEncoder<RpcResponse> {

    protected void encode(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse, ByteBuf byteBuf) throws Exception {
        String jsonString = JSON.toJSONString(rpcResponse);
        byte[] bytes = jsonString.getBytes();
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }
}
