package com.starter.irpc;

import com.starter.irpc.zk.CuratorClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-21 10:16
 **/
@SpringBootTest
public class Test {


    static CuratorClient client = new CuratorClient("120.79.220.182:2181");

//    public static void main(String[] args) throws Exception {
//
//        process("/com.dubbo.common.service.OrderService", 1);
//    }

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
