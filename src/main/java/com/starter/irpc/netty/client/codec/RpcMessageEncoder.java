package com.starter.irpc.netty.client.codec;

import com.starter.irpc.domain.RpcMessage;
import com.starter.irpc.utils.KryoUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 19:51
 **/
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf)  {
        byte[] bytes = KryoUtils.serialize(rpcMessage);
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }
}
