package com.starter.irpc.netty.server.handler;

import com.starter.irpc.domain.RpcMessage;
import com.starter.irpc.domain.RpcMessageType;
import com.starter.irpc.domain.RpcResponseType;
import com.starter.irpc.utils.BeanUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

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
            RpcMessage rpcRequest = (RpcMessage) msg;
            if (rpcRequest.getType() != RpcMessageType.request){
                return;
            }
            Object bean = BeanUtils.getBean(rpcRequest.getClazz());
            String[] paramsType = rpcRequest.getParamsType();
            Class paramsTypes[] = new Class[paramsType.length];
            RpcMessage rpcResponse = new RpcMessage(rpcRequest.getId());
            rpcResponse.setType(RpcMessageType.response);
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
                rpcResponse.setResultType(RpcResponseType.SUCCESS);
                rpcResponse.setRet(ret);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                rpcResponse.setResultType(RpcResponseType.FAILED);
            }
            ctx.writeAndFlush(rpcResponse);
        });

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("??????20????????????????????????????????????");
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            //????????????
            if (event.state() == IdleState.READER_IDLE){
                ctx.channel().close();
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("????????????");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("????????????");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx,cause);
        System.out.println("??????");
        cause.printStackTrace();
        ctx.close();
    }
}
