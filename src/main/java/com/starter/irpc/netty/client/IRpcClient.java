package com.starter.irpc.netty.client;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.annotation.IRpcAutoWired;
import com.starter.irpc.cglibProxy.CglibProxyFactory;
import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.domain.RpcService;
import com.starter.irpc.netty.client.handler.RpcClientHandler;
import com.starter.irpc.netty.client.handler.RpcFuture;
import com.starter.irpc.netty.server.IRpcServer;
import com.starter.irpc.route.RouteBalance;
import com.starter.irpc.utils.ProxyMap;
import com.starter.irpc.zk.CuratorClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 22:13
 **/
public class IRpcClient implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(IRpcClient.class);
    private String scanPath;
    @Autowired
    private CuratorClient curatorClient;
    @Autowired
    private RouteBalance routeBalance;
    public IRpcClient(String scanPath) {
        this.scanPath = scanPath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Reflections reflections = new Reflections(scanPath, new FieldAnnotationsScanner());
        Set<Field> fields = reflections.getFieldsAnnotatedWith(IRpcAutoWired.class);
        if (!CollectionUtils.isEmpty(fields)) {
            for (Field field : fields) {
                field.setAccessible(true); //设置private对象也可注入
                IRpcAutoWired annotation = field.getAnnotation(IRpcAutoWired.class);
                String serviceName = annotation.value();

                try {
                    List<byte[]> all = curatorClient.getAllRegister();
                    for (byte[] bytes: all){
                        RpcService rpcService = JSON.parseObject(bytes, RpcService.class);
                        routeBalance.addService(rpcService);
                    }
                    if (routeBalance.contain(serviceName)) {
                        Object bean = applicationContext.getBean(field.getDeclaringClass());
                        Object proxy = CglibProxyFactory.getProxy(field.getType());
                        ProxyMap.put(proxy, serviceName);
                        field.set(bean, proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RpcFuture sendRequest(RpcRequest rpcRequest) {
        NioEventLoopGroup work = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .handler(new IRpcClientInitializer());
        try {
            RouteBalance.point point = routeBalance.load(rpcRequest.getClazz());
            ChannelFuture cf = bootstrap.connect(point.ip, point.port).sync();
            Channel channel = cf.channel();
            RpcFuture rpcFuture = new RpcFuture();
            try {
                ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest).sync();
                RpcClientHandler.addWaiter(rpcRequest.getId(), rpcFuture);
                if (!channelFuture.isSuccess()) {
                    logger.error("send rpcRequest error");
                }
            } catch (InterruptedException e) {
                logger.error("send rpcRequest error");
            }
            return rpcFuture;
        } catch (InterruptedException e) {
            return null;
        }
    }
}
