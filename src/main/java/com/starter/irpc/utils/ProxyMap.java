package com.starter.irpc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-08-14 23:33
 **/
public class ProxyMap {
    private static Map<Object,String> map = new HashMap<>();

    public static void put(Object proxy,String serviceName){
        map.put(proxy,serviceName);
    }

    public static String getServiceName(Object proxy){
        return map.get(proxy);
    }
}

