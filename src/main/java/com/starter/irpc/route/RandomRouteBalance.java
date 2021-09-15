package com.starter.irpc.route;

import com.starter.irpc.domain.RpcService;

import java.util.List;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-14 22:31
 **/
public class RandomRouteBalance extends RouteBalance {

    @Override
    public point load(String serviceName) {
        List<RpcService> rpcServices = serviceMap.get(serviceName);
        int index = (int) (Math.random() * 1000000) % rpcServices.size();
        RpcService rpcService = rpcServices.get(index);
        return new point(rpcService.getIp(),rpcService.getPort());
    }
}
