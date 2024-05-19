package com.creatorsn.fabulous.service;

import org.springframework.stereotype.Service;

@Service
public interface TranslateService {
    String baiduTranslate(String text, String from, String to);

    String youdaoTranslate(String q,String from,String to);
}
