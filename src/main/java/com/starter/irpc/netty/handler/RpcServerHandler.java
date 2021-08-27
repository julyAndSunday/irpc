package com.starter.irpc.netty.handler;

import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.domain.RpcResponse;
import com.starter.irpc.utils.BeanUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 16:55
 **/
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server read:"+msg);
        RpcRequest rpcRequest = (RpcRequest) msg;

        Object bean = BeanUtils.getBean(rpcRequest.getClazz());
        String[] paramsType = rpcRequest.getParamsType();
        Class paramsTypes[] = new Class[paramsType.length];
        for (int i = 0; i < paramsType.length; i++) {
            paramsTypes[i] = Class.forName(paramsType[i]);
        }
        Method method = bean.getClass().getMethod(rpcRequest.getMethod(), paramsTypes);
        Object ret = method.invoke(bean, rpcRequest.getParams());
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setId(rpcRequest.getId());
        rpcResponse.setRet(ret);
        ctx.writeAndFlush(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
