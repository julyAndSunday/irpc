package com.starter.irpc;

import com.starter.irpc.annotation.IRpcAutoWired;
import com.starter.irpc.zk.CuratorClient;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 10:16
 **/
@SpringBootTest
public class Test  {


    static CuratorClient client = new CuratorClient("120.79.220.182:2181");

    public static void main(String[] args) throws Exception {

        process("/com.dubbo.common.service.OrderService", 1);
    }

    private static void process(String name, int level) throws Exception {
        List<String> children = client.getChildren(name);
        if (!CollectionUtils.isEmpty(children)) {
            for (String child : children) {
                int temp = level;
                while (temp-- != 0) {
                    System.out.print("-");
                }
                System.out.println(child);
                process(name + "/" + child, level + 1);
            }
        }
    }

}
