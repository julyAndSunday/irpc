package com.starter.irpc.netty.client;

import com.starter.irpc.netty.client.handler.RpcFuture;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-22 09:38
 **/
public class RpcCache {
    private static ConcurrentHashMap<Long, RpcFuture> waitingRPC = new ConcurrentHashMap<>();

    public static RpcFuture get(Long id){
        return waitingRPC.get(id);
    }

    public static void put(Long id,RpcFuture rpcFuture){
        waitingRPC.put(id,rpcFuture);
    }
}
