package com.starter.irpc.utils;

import com.starter.irpc.domain.RpcResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;

/**
 * @Description: rpc请求表
 * @Author: July
 * @Date: 2021-08-13 15:00
 **/
public class WailMap {

    //请求id -- 响应
    private static ConcurrentHashMap<Long, RpcResponse> responseMap = new ConcurrentHashMap<>();
    //请求id -- 条件变量
    private static ConcurrentHashMap<Long, CountDownLatch> countMap = new ConcurrentHashMap<>();


    public static void addRes(RpcResponse rpcResponse){
        responseMap.put(rpcResponse.getId(),rpcResponse);
    }

    public static boolean contain(Long id){
        return responseMap.contains(id);
    }

    public static RpcResponse poll(Long id){
        RpcResponse rpcResponse = responseMap.get(id);
        responseMap.remove(id);
        return rpcResponse;
    }

    public static void addCondition(Long id, CountDownLatch countDownLatch) {
        countMap.put(id,countDownLatch);
    }

    public static void notifyById(long id) {
        CountDownLatch countDownLatch = countMap.get(id);
        countDownLatch.countDown();
        countMap.remove(id);
    }
}
