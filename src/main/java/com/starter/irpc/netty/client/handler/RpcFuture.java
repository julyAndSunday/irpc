package com.starter.irpc.netty.client.handler;

import com.starter.irpc.domain.RpcResponse;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-09 15:53
 **/
public class RpcFuture {

    private RpcResponse rpcResponse;
    private boolean done;

    public RpcFuture( ) {
        done = false;
    }

    public synchronized RpcResponse get(){
        while (!done) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return rpcResponse;
    }

    public synchronized void setRpcResponse(RpcResponse rpcResponse){
        if (done)return;

        this.rpcResponse = rpcResponse;
        done = true;
        notify();
    }




}
