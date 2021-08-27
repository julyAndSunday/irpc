package com.starter.irpc.netty.codec;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.domain.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 19:51
 **/
public class RpcRequestEncoder extends MessageToByteEncoder<RpcRequest> {

    protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest, ByteBuf byteBuf) throws Exception {
        String jsonString = JSON.toJSONString(rpcRequest);
        byte[] bytes = jsonString.getBytes();
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }
}
