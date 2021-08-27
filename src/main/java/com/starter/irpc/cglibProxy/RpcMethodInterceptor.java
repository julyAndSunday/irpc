package com.starter.irpc.cglibProxy;

import com.starter.irpc.domain.RpcRequest;
import com.starter.irpc.netty.client.IRpcClient;
import com.starter.irpc.utils.ProxyMap;
import com.starter.irpc.utils.WailMap;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 15:26
 **/
public class RpcMethodInterceptor implements MethodInterceptor {


    /**
     * @param o           被代理的对象（需要增强的对象）
     * @param method      被拦截的方法（需要增强的方法）
     * @param args        方法入参
     * @param methodProxy 用于调用原始方法
     */
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("toString") ||methodName.equals("hashCode")){
            return methodProxy.invokeSuper(o,args); //调用原始方法  不能使用invoke() 这样调用的是代理的方法 导致栈溢出
        }
        RpcRequest rpcRequest = generateRpcRequest(o, method, args);
        IRpcClient.sendRequest(rpcRequest);
        Long id = rpcRequest.getId();
        CountDownLatch countDownLatch = new CountDownLatch(1); //获取同步计算器
        WailMap.addCondition(id,countDownLatch);
        countDownLatch.await();
        return WailMap.poll(id).getRet();
    }

    private RpcRequest generateRpcRequest(Object target,Method method,Object[] args){
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
}