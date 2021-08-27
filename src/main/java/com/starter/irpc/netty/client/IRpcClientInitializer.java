package com.starter.irpc.netty.client;

import com.starter.irpc.netty.codec.RpcRequestEncoder;
import com.starter.irpc.netty.codec.RpcResponseDecoder;
import com.starter.irpc.netty.handler.RpcClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 22:16
 **/
public class IRpcClientInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new RpcRequestEncoder())
                .addLast(new RpcResponseDecoder())
                .addLast(new RpcClientHandler());
    }
}
