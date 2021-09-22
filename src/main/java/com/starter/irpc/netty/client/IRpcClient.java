package com.starter.irpc.netty.client;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.annotation.IRpcAutoWired;
import com.starter.irpc.cglibProxy.CglibProxyFactory;
import com.starter.irpc.domain.RpcMessage;
import com.starter.irpc.domain.RpcService;
import com.starter.irpc.netty.client.handler.RpcFuture;
import com.starter.irpc.route.RouteBalance;
import com.starter.irpc.utils.ProxyMap;
import com.starter.irpc.utils.RpcCache;
import com.starter.irpc.zk.CuratorClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 22:13
 **/
public class IRpcClient implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(IRpcClient.class);
    private String scanPath;
    private Bootstrap bootstrap;
    @Autowired
    private CuratorClient curatorClient;
    @Autowired
    private RouteBalance routeBalance;
    private ThreadPoolExecutor executor;

    public IRpcClient(String scanPath) {
        this.scanPath = scanPath;
    }

    public RpcMessage sendRequest(RpcMessage rpcRequest) {
        RpcFuture rpcFuture = new RpcFuture();
        executor.execute(()->{
            try {
                RouteBalance.point point = routeBalance.load(rpcRequest.getClazz());
               Channel channel =  RpcCache.getChannel(point);
                if (channel==null){
                    ChannelFuture cf = bootstrap.connect(point.ip, point.port).sync();
                    channel = cf.channel();
                    RpcCache.putChannel(point, channel);
                }
                ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest).sync();
                //添加future 后续获取值
                RpcCache.putFuture(rpcRequest.getId(),rpcFuture);
                channelFuture.channel().closeFuture().addListeners((ChannelFutureListener) channelFuture1 -> {
                    RpcCache.removeChannel(point);
                    System.out.println("连接断开");
                });
                if (!channelFuture.isSuccess()) {
                    logger.error("send rpcRequest error");
                }
            } catch (InterruptedException e) {
                logger.error("send rpcRequest error");
            }
        });
        //阻塞获取
        RpcMessage rpcMessage = rpcFuture.get();
        RpcCache.removeFuture(rpcMessage.getId());
        return rpcMessage;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Reflections reflections = new Reflections(scanPath, new FieldAnnotationsScanner());
        Set<Field> fields = reflections.getFieldsAnnotatedWith(IRpcAutoWired.class);
        if (!CollectionUtils.isEmpty(fields)) {
            for (Field field : fields) {
                field.setAccessible(true); //设置private对象也可注入
                IRpcAutoWired annotation = field.getAnnotation(IRpcAutoWired.class);
                String serviceName = annotation.value();

                try {
                    List<byte[]> all = curatorClient.getAllRegister();
                    //放入routeBalance 用于负载均衡
                    for (byte[] bytes : all) {
                        RpcService rpcService = JSON.parseObject(bytes, RpcService.class);
                        routeBalance.addService(rpcService);
                    }
                    if (routeBalance.contain(serviceName)) {
                        Object bean = applicationContext.getBean(field.getDeclaringClass());
                        IRpcClient iRpcClient = applicationContext.getBean(IRpcClient.class);
                        Object proxy = CglibProxyFactory.getProxy(field.getType(),iRpcClient);
                        ProxyMap.put(proxy, serviceName);
                        field.set(bean, proxy);
                        System.out.println(bean);
                    }

                    //初始化netty客户端连接
                    NioEventLoopGroup work = new NioEventLoopGroup();
                    bootstrap = new Bootstrap();
                    bootstrap.group(work)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new IRpcClientInitializer());

                    //初始化线程池
                    executor = new ThreadPoolExecutor(4,6,5,TimeUnit.MINUTES,new LinkedBlockingDeque<>(20));;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
