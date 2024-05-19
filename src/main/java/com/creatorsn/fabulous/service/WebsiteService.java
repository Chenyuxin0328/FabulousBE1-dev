package com.creatorsn.fabulous.service;

import com.creatorsn.fabulous.entity.WebsiteConfig;
import org.springframework.stereotype.Service;

/**
 * 网站配置服务
 */
@Service
public interface WebsiteService {

    /**
     * 创建网站配置
     *
     * @param config 配置
     * @return 配置
     */
    WebsiteConfig create(WebsiteConfig config);

    /**
     * 获取网站配置
     *
     * @param name 配置名称
     * @return 配置
     */
    WebsiteConfig get(String name);
}
