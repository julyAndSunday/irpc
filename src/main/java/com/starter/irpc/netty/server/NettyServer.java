package com.starter.irpc.netty.server;

import com.starter.irpc.domain.RpcService;
import com.starter.irpc.zk.CuratorClient;
import com.starter.irpc.zk.ZkConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-14 20:14
 **/
public class NettyServer {

    private String ip;
    private int port;
    private Thread thread;
    private Logger logger = LoggerFactory.getLogger(IRpcServer.class);
    private Map<String, RpcService> serviceMap = new HashMap<>();
    @Autowired
    private CuratorClient curatorClient;

    public NettyServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }


    public void addService(RpcService rpcService) throws Exception {
        serviceMap.put(rpcService.getServiceName(),rpcService);
        curatorClient.createPathData(ZkConstant.ZK_REGISTER+rpcService.generateServiceKey(),rpcService.toJson().getBytes());
    }

    public void start() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,10,5, TimeUnit.MINUTES,new LinkedBlockingDeque<>(20));;
        //线程关闭
        thread = new Thread(() -> {
            NioEventLoopGroup bossGroup =new NioEventLoopGroup();
            NioEventLoopGroup workGroup =new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new IRpcServerInitializer(threadPoolExecutor));

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
        });
        thread.start();
    }

    public void shutdown(){
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
