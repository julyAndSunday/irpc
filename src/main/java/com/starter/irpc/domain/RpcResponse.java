package com.starter.irpc.domain;


/**
 * @Description:
 * @Author: July
 * @Date: 2021-08-11 21:46
 **/
public class RpcResponse {
    private long id;
    private int code;
    private Object ret;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RpcResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getRet() {
        return ret;
    }

    public void setRet(Object ret) {
        this.ret = ret;
    }
}
