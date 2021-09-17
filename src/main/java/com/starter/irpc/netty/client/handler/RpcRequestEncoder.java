package com.starter.irpc.netty.client.handler;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.utils.KryoUtils;
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
        byte[] bytes = KryoUtils.serialize(rpcRequest);
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }
}
