package com.creatorsn.fabulous.util.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author minskiter
 * @date 27/8/2023 17:41
 * @description Minio 配置项
 */
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfiguration {

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 访问钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 篮子的名字
     */
    private String bucket;

    public String getHost() {
        return host;
    }

    public MinioConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public MinioConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public MinioConfiguration setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public MinioConfiguration setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public MinioConfiguration setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
}
