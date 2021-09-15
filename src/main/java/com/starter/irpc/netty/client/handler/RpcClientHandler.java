package com.starter.irpc.netty.client.handler;

import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.domain.RpcResponse;
import com.starter.irpc.netty.client.IRpcClient;
import com.starter.irpc.utils.WailMap;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 16:11
 **/
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private SocketAddress remotePeer;
    private static volatile Channel channel;
    private static ConcurrentHashMap<Long, RpcFuture> waitingRPC = new ConcurrentHashMap<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = channel.remoteAddress();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        System.out.println("server read:"+rpcResponse);
        waitingRPC.get(rpcResponse.getId()).setRpcResponse(rpcResponse);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //Send ping
//            sendRequest(Beat.BEAT_PING);
//            logger.debug("Client send beat-ping to " + remotePeer);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    public static void addWaiter(Long id,RpcFuture rpcFuture){
        waitingRPC.put(id,rpcFuture);
    }
}
