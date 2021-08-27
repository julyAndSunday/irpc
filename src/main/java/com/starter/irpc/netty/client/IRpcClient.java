package com.starter.irpc.netty.client;

import com.starter.irpc.domain.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 22:13
 **/
public class IRpcClient {
    private String ip;
    private int port;
    private static Channel channel;


    public IRpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start(){
        new Thread(()->{
            NioEventLoopGroup work = new NioEventLoopGroup();

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(work)
                    .channel(NioSocketChannel.class)
                    .handler(new IRpcClientInitializer());
            try {
                ChannelFuture cf = bootstrap.connect(ip, port).sync();
                channel = cf.channel();
                cf.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void sendRequest(Object msg){
        channel.writeAndFlush(msg);
    }

    public static void main(String[] args) {
        IRpcClient iRpcClient = new IRpcClient("localhost", 20880);
        iRpcClient.start();
    }
}
