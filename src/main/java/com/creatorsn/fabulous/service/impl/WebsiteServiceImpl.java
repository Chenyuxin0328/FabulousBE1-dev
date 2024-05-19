package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.entity.WebsiteConfig;
import com.creatorsn.fabulous.mapper.WebsiteConfigMapper;
import com.creatorsn.fabulous.service.WebsiteService;
import com.creatorsn.fabulous.util.RandomUtil;
import org.springframework.stereotype.Service;

@Service
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteConfigMapper mapper;

    public WebsiteServiceImpl(WebsiteConfigMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public WebsiteConfig create(WebsiteConfig config) {
        config.setId(RandomUtil.DateTimeUUID());
        return mapper.create(config);
    }

    @Override
    public WebsiteConfig get(String name) {
        return mapper.get(name);
    }
}
