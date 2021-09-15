package com.starter.irpc.netty.server;

import com.starter.irpc.annotation.IRpc;
import com.starter.irpc.annotation.IRpcAutoWired;
import com.starter.irpc.domain.RpcService;
import com.starter.irpc.zk.ZkConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 15:22
 **/
public class IRpcServer extends NettyServer implements ApplicationListener<ContextRefreshedEvent> {

    public IRpcServer(String ip, int port) {
        super(ip, port);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(IRpc.class);
        if (!CollectionUtils.isEmpty(beanMap)) {
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Class clazz = entry.getValue().getClass();
                String service = entry.getKey();
                RpcService rpcService = new RpcService();
                rpcService.setServiceName(service);
                rpcService.setClazz(clazz);
                rpcService.setIp(getIp());
                rpcService.setPort(getPort());
                try {
                    //服务注册
                    addService(rpcService);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            start();
        }
    }
}