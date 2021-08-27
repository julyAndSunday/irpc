package com.starter.irpc.domain;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-08-11 17:30
 **/
public class RpcService {
    private String name;
    private Class clazz;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
