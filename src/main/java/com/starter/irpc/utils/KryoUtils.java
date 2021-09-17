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
    private static Kryo kryo = new Kryo();


    public static byte[] serialize(Object o){
        kryo.register(o.getClass());
        Output output = new Output(1024,-1);
        kryo.writeObject(output,o);
        return output.getBuffer();
    }

    public static  <T> T  deserialize(byte[] bytes,Class<T> clazz){
        Input input = new Input(bytes);
        return kryo.readObject(input,clazz);
    }
}
