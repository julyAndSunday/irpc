package com.starter.irpc.cglibProxy;

import com.starter.irpc.domain.RpcMessage;
import com.starter.irpc.domain.RpcMessageType;
import com.starter.irpc.netty.client.IRpcClient;
import com.starter.irpc.utils.ProxyMap;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

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
        RpcMessage rpcRequest = generateRpcRequest(o, method, args);
        RpcMessage rpcResponse = iRpcClient.sendRequest(rpcRequest);
        return rpcResponse.getRet();
    }

    private RpcMessage generateRpcRequest(Object target, Method method, Object[] args) {
        RpcMessage rpcRequest = new RpcMessage();
        rpcRequest.setType(RpcMessageType.request);
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