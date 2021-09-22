package com.starter.irpc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: July
 * @Date: 2021-07-19 23:28
 **/
@ConfigurationProperties(prefix = "irpc")
public class IRpcProperties {

    public String serverHost;
    public int serverPort;
    public String zkHost;
    public int zkPort;
    public String scanPath;

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }

    public void setZkPort(int zkPort) {
        this.zkPort = zkPort;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }
}
