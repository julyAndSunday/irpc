package com.starter.irpc.netty.server;

import com.starter.irpc.annotation.IRpcAutoWired;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 15:22
 **/
public class IRpcServer {
    private String ip;
    private int port;
    private Logger logger = LoggerFactory.getLogger(IRpcServer.class);


    public IRpcServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void start() {
        //线程关闭
        new Thread(() -> {
            NioEventLoopGroup bossGroup =new NioEventLoopGroup();
            NioEventLoopGroup workGroup =new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new IRpcServerInitializer());

            //对关闭通道进行监听
            try {
                //绑定一个端口并且同步
                //启动服务器
                ChannelFuture cf = serverBootstrap.bind(ip,port).sync();
                logger.info("start");
                cf.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                workGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();
    }

    public static void main(String[] args) {
        IRpcServer iRpcServer = new IRpcServer("localhost", 20880);
        iRpcServer.start();
    }
}
