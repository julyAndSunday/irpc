package com.starter.irpc.cglibProxy;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @Description:  默认返回第一个拦截器  在有多个拦截器时使用
 * @Author: July
 * @Date: 2021-08-15 14:26
 **/
public class MethodFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        return 0;
    }
}
