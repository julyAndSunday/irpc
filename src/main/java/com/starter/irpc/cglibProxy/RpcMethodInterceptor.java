package com.starter.irpc.cglibProxy;

import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.domain.RpcResponse;
import com.starter.irpc.netty.client.IRpcClient;
import com.starter.irpc.netty.client.handler.RpcFuture;
import com.starter.irpc.utils.ProxyMap;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 15:26
 **/
public class RpcMethodInterceptor implements MethodInterceptor {
    private IRpcClient iRpcClient;

    public RpcMethodInterceptor(IRpcClient iRpcClient) {
        this.iRpcClient = iRpcClient;
    }

    /**
     * @param o           被代理的对象（需要增强的对象）
     * @param method      被拦截的方法（需要增强的方法）
     * @param args        方法入参
     * @param methodProxy 用于调用原始方法
     */
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("toString") || methodName.equals("hashCode")) {
            return methodProxy.invokeSuper(o, args); //调用原始方法  不能使用invoke() 这样调用的是代理的方法 导致栈溢出
        }
        RpcRequest rpcRequest = generateRpcRequest(o, method, args);
        RpcResponse rpcResponse = iRpcClient.sendRequest(rpcRequest);
        return rpcResponse.getRet();
    }

    private RpcRequest generateRpcRequest(Object target, Method method, Object[] args) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClazz(ProxyMap.getServiceName(target));
        rpcRequest.setMethod(method.getName());
        String[] paramsType = new String[args.length];
        for (int i = 0; i < paramsType.length; i++) {
            paramsType[i] = args[i].getClass().getName();
        }
        rpcRequest.setParamsType(paramsType);
        rpcRequest.setParams(args);
        return rpcRequest;
    }

    public static void main(String[] args) {

        System.out.println("main函数开始执行");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                System.out.println("===task start===");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("===task finish===");
                return 3;
            }
        }, executor);

        future.thenAccept(e -> System.out.println(e));
        System.out.println("main函数执行结束");
        executor.shutdown();
    }
}