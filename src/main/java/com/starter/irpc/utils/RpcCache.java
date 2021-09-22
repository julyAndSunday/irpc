package com.starter.irpc.utils;

import com.starter.irpc.netty.client.handler.RpcFuture;
import com.starter.irpc.route.RouteBalance;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-22 09:38
 **/
public class RpcCache {
    private static ConcurrentHashMap<Long, RpcFuture> waitingRPC = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<RouteBalance.point, Channel> connectCache = new ConcurrentHashMap<>();

    public static RpcFuture getFuture(Long id){
        return waitingRPC.get(id);
    }

    public static void putFuture(Long id, RpcFuture rpcFuture){
        waitingRPC.put(id,rpcFuture);
    }

    public static void removeFuture(Long id) {
        waitingRPC.remove(id);
    }

    public static Channel getChannel(RouteBalance.point point){
        return connectCache.get(point);
    }

    public static void putChannel(RouteBalance.point point, Channel channel){
        connectCache.put(point,channel);
    }


    public static void removeChannel(RouteBalance.point point) {
        connectCache.remove(point);
    }
}
