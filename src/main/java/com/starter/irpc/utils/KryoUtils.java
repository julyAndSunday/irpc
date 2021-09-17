package com.starter.irpc.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.starter.irpc.domain.RpcRequest;
import io.protostuff.Rpc;

import java.io.*;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-09-17 11:27
 **/
public class KryoUtils {
    //kryo池 本身线程不安全 需要每一个线程一个kryo
    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            // configure kryo instance, customize settings
            return kryo;
        };
    };


    public static byte[] serialize(Object o){
        Kryo kryo = kryos.get();
        kryo.register(o.getClass());
        Output output = new Output(1024,-1);
        kryo.writeObject(output,o);
        return output.getBuffer();
    }

    public static  <T> T  deserialize(byte[] bytes,Class<T> clazz){
        Kryo kryo = kryos.get();
        Input input = new Input(bytes);
        return kryo.readObject(input,clazz);
    }
}
