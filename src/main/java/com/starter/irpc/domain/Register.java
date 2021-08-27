package com.starter.irpc.domain;


import com.starter.irpc.annotation.IRpcAutoWired;

import java.util.List;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-22 23:07
 **/
public class Register {
    private String ip;
    private int port;
    private List<RpcService> services;

    public List<RpcService> getServices() {
        return services;
    }

    public void setServices(List<RpcService> services) {
        this.services = services;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
