package com.starter.irpc.netty.client.handler;

import com.starter.irpc.domain.RpcMessage;
import com.starter.irpc.domain.RpcMessageType;
import com.starter.irpc.utils.RpcCache;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 16:11
 **/
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private SocketAddress remotePeer;
    private static volatile Channel channel;
    private int lossCount = 0;
    private final int MAX_PING = 5;

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

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        lossCount = 0;
        RpcCache.getFuture(rpcMessage.getId()).setRpcResponse(rpcMessage);
    }

    //发送ping包
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.READER_IDLE && lossCount<MAX_PING){
                logger.info("send ping");
                lossCount++;
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setType(RpcMessageType.ping);
                ctx.writeAndFlush(rpcMessage);
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

}
