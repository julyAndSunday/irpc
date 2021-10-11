package com.starter.irpc.config;

import com.starter.irpc.netty.client.IRpcClient;
import com.starter.irpc.netty.server.IRpcServer;
import com.starter.irpc.properties.IRpcProperties;
import com.starter.irpc.route.RandomRouteBalance;
import com.starter.irpc.route.RouteBalance;
import com.starter.irpc.zk.CuratorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-19 23:29
 **/
@Configuration
@EnableConfigurationProperties(IRpcProperties.class)
@ConditionalOnProperty(prefix = "irpc", value = "enabled", matchIfMissing = true)
public class IRpcAutoConfiguration {

    @Autowired
    private IRpcProperties iRpcProperties;

    @Bean
    public CuratorClient curatorClient() {
        return new CuratorClient(iRpcProperties.zkHost, iRpcProperties.zkPort);
    }

    @Bean
    public IRpcServer iRpcServer() {
        return new IRpcServer(iRpcProperties.serverHost, iRpcProperties.serverPort);
    }

    @Bean
    @Conditional(ClientCondition.class)
    public IRpcClient iRpcClient() {
        return new IRpcClient(iRpcProperties.scanPath,iRpcProperties.timeout);
    }

    @Bean
    public RouteBalance routeBalance() {
        return new RandomRouteBalance();
    }

}
