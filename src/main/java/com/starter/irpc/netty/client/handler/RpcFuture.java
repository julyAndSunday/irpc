package com.starter.irpc.netty.client.handler;

import com.starter.irpc.domain.RpcMessage;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-09 15:53
 **/
public class RpcFuture {

    private RpcMessage rpcMessage;
    private boolean done;

    public RpcFuture( ) {
        done = false;
    }

    public synchronized RpcMessage get(){
        while (!done) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return rpcMessage;
    }

    public synchronized void setRpcResponse(RpcMessage rpcMessage){
        if (done)return;

        this.rpcMessage = rpcMessage;
        done = true;
        notify();
    }
}
