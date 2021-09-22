package com.starter.irpc.netty.server.handler;

import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.domain.RpcResponse;
import com.starter.irpc.utils.BeanUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 16:55
 **/
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private final ThreadPoolExecutor serverHandlerPool;

    public RpcServerHandler(ThreadPoolExecutor serverHandlerPool) {
        this.serverHandlerPool = serverHandlerPool;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("server read "+msg);
        serverHandlerPool.execute(()->{
            RpcRequest rpcRequest = (RpcRequest) msg;
            Object bean = BeanUtils.getBean(rpcRequest.getClazz());
            String[] paramsType = rpcRequest.getParamsType();
            Class paramsTypes[] = new Class[paramsType.length];
            RpcResponse rpcResponse = new RpcResponse();
            for (int i = 0; i < paramsType.length; i++) {
                try {
                    paramsTypes[i] = Class.forName(paramsType[i]);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                Method method = bean.getClass().getMethod(rpcRequest.getMethod(), paramsTypes);
                Object ret = method.invoke(bean, rpcRequest.getParams());
                rpcResponse.setCode(200);
                rpcResponse.setRet(ret);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                rpcResponse.setCode(500);
                rpcResponse.setRet(null);
            }
            rpcResponse.setId(rpcRequest.getId());
            ctx.writeAndFlush(rpcResponse);
        });

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常");
        cause.printStackTrace();
        ctx.close();
    }
}
