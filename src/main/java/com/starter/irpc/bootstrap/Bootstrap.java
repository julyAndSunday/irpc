package com.starter.irpc.bootstrap;

import com.alibaba.fastjson.JSON;
import com.starter.irpc.annotation.IRpc;
import com.starter.irpc.annotation.IRpcAutoWired;
import com.starter.irpc.cglibProxy.CglibProxyFactory;
import com.starter.irpc.domain.Register;
import com.starter.irpc.domain.RpcService;
import com.starter.irpc.netty.client.IRpcClient;
import com.starter.irpc.netty.server.IRpcServer;
import com.starter.irpc.utils.ProxyMap;
import com.starter.irpc.zk.CuratorClient;
import com.starter.irpc.zk.ZkConstant;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 监听器 监听ApplicationContextEvent  直到所有bean注入后调用
 * @Author: July
 * @Date: 2021-08-13 10:11
 **/
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private CuratorClient curatorClient;
    private IRpcServer iRpcServer;
    private IRpcClient iRpcClient;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        curatorClient = applicationContext.getBean(CuratorClient.class);
        iRpcServer = applicationContext.getBean(IRpcServer.class);
        iRpcClient = applicationContext.getBean(IRpcClient.class);
        registerServicesAndStartServer(applicationContext);
        injectServiceAndStartClient(applicationContext);
    }

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path.replaceAll("\\\\",".");
        System.out.println(path);
        Reflections reflections = new Reflections("com.starter.irpc", new FieldAnnotationsScanner());
        Set<Field> fields = reflections.getFieldsAnnotatedWith(IRpc.class);
        System.out.println(fields.size());

    }
    private void injectServiceAndStartClient(ApplicationContext applicationContext) {
        Reflections reflections = new Reflections("com.test.consumer", new FieldAnnotationsScanner());
        Set<Field> fields = reflections.getFieldsAnnotatedWith(IRpcAutoWired.class);
        if (!CollectionUtils.isEmpty(fields)) {
            for (Field field : fields) {
                field.setAccessible(true); //设置private对象也可注入
                IRpcAutoWired annotation = field.getAnnotation(IRpcAutoWired.class);
                String serviceName = annotation.value();
                try {
                    if (curatorClient.containService(serviceName)) {
                        Object bean = applicationContext.getBean(field.getDeclaringClass());
                        Object proxy = CglibProxyFactory.getProxy(field.getType());
                        ProxyMap.put(proxy,serviceName);
                        field.set(bean, proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iRpcClient.start();
        }

    }

    private void registerServicesAndStartServer(ApplicationContext applicationContext) {

        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(IRpc.class);
        if (!CollectionUtils.isEmpty(beanMap)) {
            String serviceJson;
            List<RpcService> services = new ArrayList<RpcService>();
            for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
                Class clazz = entry.getValue().getClass();
                String service = entry.getKey();
                RpcService rpcService = new RpcService();
                rpcService.setName(service);
                rpcService.setClazz(clazz);
                services.add(rpcService);
                Register register = new Register();
                register.setIp(iRpcServer.getIp());
                register.setPort(iRpcServer.getPort());
                register.setServices(services);
                serviceJson = JSON.toJSONString(register);
                try {
                    //服务注册
                    System.out.println(service + ":" + serviceJson);
                    curatorClient.createPathData(ZkConstant.ZK_REGISTER + "/" + service, serviceJson.getBytes());
                    logger.info("register service:" + service);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iRpcServer.start();
        }
    }

}
