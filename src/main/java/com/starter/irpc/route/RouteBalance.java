package com.starter.irpc.route;

import com.starter.irpc.domain.RpcService;

import java.util.*;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            point point = (point) o;
            return port == point.port &&
                    Objects.equals(ip, point.ip);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, port);
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
