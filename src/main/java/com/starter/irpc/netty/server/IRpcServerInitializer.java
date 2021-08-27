package com.starter.irpc.netty.server;

import com.starter.irpc.netty.codec.RpcRequestDecoder;
import com.starter.irpc.netty.codec.RpcResponseEncoder;
import com.starter.irpc.netty.handler.RpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 16:39
 **/
public class IRpcServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new RpcRequestDecoder())
                .addLast(new RpcResponseEncoder())
                .addLast(new RpcServerHandler());
    }
}
