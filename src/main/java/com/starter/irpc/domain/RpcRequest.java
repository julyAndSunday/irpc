package com.starter.irpc.domain;

import lombok.Data;


/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 16:49
 **/
@Data
public class RpcRequest  {

    private Long id;
    private String clazz;
    private String method;
    private String[] paramsType;
    private Object[] params;
    private Object ret;

    public RpcRequest()  {
        id = System.currentTimeMillis();
    }
}
