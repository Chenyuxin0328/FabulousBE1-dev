package com.creatorsn.fabulous.util.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author minskiter
 * @date 24/8/2023 22:28
 * @description
 */
@Component
@ConfigurationProperties(prefix = "weedfs")
public class WeedFileConfiguration implements Serializable {

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    public String getHost() {
        return host;
    }

    public WeedFileConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public WeedFileConfiguration setPort(int port) {
        this.port = port;
        return this;
    }
}
