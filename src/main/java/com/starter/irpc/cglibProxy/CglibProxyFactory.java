package com.starter.irpc.cglibProxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 15:27
 **/
public class CglibProxyFactory {
    public static Object getProxy(Class<?> clazz) {
        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(clazz);
        //设置方法过滤器
//        enhancer.setCallbackFilter(new MethodFilter());
        // 设置方法拦截器
        enhancer.setCallback(new RpcMethodInterceptor());
        // 创建代理类
        return enhancer.create();
    }
}
