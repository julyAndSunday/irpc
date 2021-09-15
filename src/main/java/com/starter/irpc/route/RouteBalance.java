package com.starter.irpc.route;

import com.starter.irpc.domain.RpcService;
import io.protostuff.Rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-14 21:18
 **/
public abstract class RouteBalance {

    Map<String, List<RpcService>> serviceMap = new HashMap<>();


    public class point{
        public String ip;
        public int port;

        public point(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }
    }

    public abstract point load(String serviceName);

    public void addService(RpcService rpcService){
        if (serviceMap.containsKey(rpcService.getServiceName())){
            serviceMap.get(rpcService.getServiceName()).add(rpcService);
        }else {
            ArrayList<RpcService> list = new ArrayList<>();
            list.add(rpcService);
            serviceMap.put(rpcService.getServiceName(),list);
        }
    }
    public  boolean contain(String serviceName){
        return serviceMap.containsKey(serviceName);
    }

}
