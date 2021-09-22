//package com.starter.irpc.utils;
//
//import com.starter.irpc.domain.RpcMessage;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CountDownLatch;
//
///**
// * @Description: rpc请求表
// * @Author: July
// * @Date: 2021-08-13 15:00
// **/
//public class WailMap {
//
//    //请求id -- 响应
//    private static ConcurrentHashMap<Long, RpcMessage> responseMap = new ConcurrentHashMap<>();
//    //请求id -- 条件变量
//    private static ConcurrentHashMap<Long, CountDownLatch> countMap = new ConcurrentHashMap<>();
//
//
//    public static void addRes(RpcMessage rpcResponse){
//        responseMap.putFuture(rpcResponse.getId(),rpcResponse);
//    }
//
//    public static boolean contain(Long id){
//        return responseMap.contains(id);
//    }
//
//    public static RpcMessage poll(Long id){
//        RpcMessage rpcResponse = responseMap.getFuture(id);
//        responseMap.remove(id);
//        return rpcResponse;
//    }
//
//    public static void addCondition(Long id, CountDownLatch countDownLatch) {
//        countMap.putFuture(id,countDownLatch);
//    }
//
//    public static void notifyById(long id) {
//        CountDownLatch countDownLatch = countMap.getFuture(id);
//        countDownLatch.countDown();
//        countMap.remove(id);
//    }
//}
