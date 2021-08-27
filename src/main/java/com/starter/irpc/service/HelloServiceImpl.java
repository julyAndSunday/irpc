package com.starter.irpc.service;

import com.starter.irpc.annotation.IRpc;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-19 23:51
 **/
@IRpc
@Service
public class HelloServiceImpl   {


    public String hello() {
        System.out.println(this.hashCode());
        return "are you ok!!!";
    }
}
