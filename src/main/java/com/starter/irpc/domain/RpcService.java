package com.starter.irpc.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-08-11 17:30
 **/
@Data
public class RpcService {
    private String serviceName;
    private String ip;
    private int port;
    private Class clazz;


    public String generateServiceKey(){
        return "/"+serviceName + ip.hashCode();
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
