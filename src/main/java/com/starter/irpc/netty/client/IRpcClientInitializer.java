package com.starter.irpc.netty.client;

import com.starter.irpc.netty.client.codec.RpcMessageDecoder;
import com.starter.irpc.netty.client.codec.RpcMessageEncoder;
import com.starter.irpc.netty.client.handler.RpcClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 22:16
 **/
public class IRpcClientInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                .addLast(new RpcMessageDecoder())
                .addLast(new RpcMessageEncoder())
                .addLast(new RpcClientHandler());
    }
}
