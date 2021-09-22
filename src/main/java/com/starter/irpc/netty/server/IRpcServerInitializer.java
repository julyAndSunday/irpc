package com.starter.irpc.netty.server;

import com.starter.irpc.netty.client.codec.RpcMessageDecoder;
import com.starter.irpc.netty.client.codec.RpcMessageEncoder;
import com.starter.irpc.netty.server.handler.RpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 16:39
 **/
public class IRpcServerInitializer extends ChannelInitializer<SocketChannel> {
    private ThreadPoolExecutor threadPoolExecutor;

    public IRpcServerInitializer(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS))
                .addLast(new RpcMessageDecoder())
                .addLast(new RpcMessageEncoder())
                .addLast(new RpcServerHandler(threadPoolExecutor));
    }
}
