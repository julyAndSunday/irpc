package com.starter.irpc.domain;

import lombok.Data;



/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 16:49
 **/
@Data
public class RpcMessage {

    private Long id;
    private int type;
    private String clazz;
    private String method;
    private String[] paramsType;
    private Object[] params;
    private Object ret;

    public RpcMessage() {
        id = System.currentTimeMillis();
    }
}
