package com.starter.irpc.netty.handler;

import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.domain.RpcResponse;
import com.starter.irpc.utils.WailMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 16:11
 **/
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        System.out.println("server read:"+rpcResponse);
        WailMap.addRes(rpcResponse);
        WailMap.notifyById(rpcResponse.getId());
    }
}
