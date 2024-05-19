package com.creatorsn.fabulous.service.impl;

import com.creatorsn.fabulous.service.TranslateService;
import com.creatorsn.fabulous.util.baidutranslate.TransApi;
import com.creatorsn.fabulous.util.youdatranslate.AuthV3Util;
import com.creatorsn.fabulous.util.youdatranslate.HttpUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TranslateServiceImpl implements TranslateService {
    @Value("${baidu.translate.appid}")
    private String appId;

    @Value("${baidu.translate.key}")
    private String key;

//    private RestTemplate restTemplate = new RestTemplate();

    public String baiduTranslate(String text, String from, String to) {
       try {
           TransApi api = new TransApi(appId, key);
           String transResult = api.getTransResult(text, from, to);

           JsonObject jsonObject = JsonParser.parseString(transResult).getAsJsonObject();
           String translation = jsonObject.getAsJsonArray("trans_result").get(0).getAsJsonObject().get("dst").getAsString();
           return translation;
       }catch (Exception e){
           e.printStackTrace();
       }return null;
    }


    @Value("${youdao.translate.appid}")
    private String APP_KEY;     // 您的应用ID
    @Value("${youdao.translate.key}")
    private String APP_SECRET;  // 您的应用密钥

    public String youdaoTranslate(String q,String from,String to) {
       try {
           Map<String, String[]> params = createRequestParams(q, from, to);
           // 添加鉴权相关参数
           AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
           // 请求api服务
           byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api", null, params, "application/json");
           String change=new String(result, StandardCharsets.UTF_8);

           JsonElement jsonElement = JsonParser.parseString(change);
           JsonObject jsonObject = jsonElement.getAsJsonObject();
           String translation = jsonObject.getAsJsonArray("translation").get(0).getAsString();
           return translation;
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }return null;

    }

    private static Map<String, String[]> createRequestParams(String q,String from,String to) {

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("from", new String[]{from});
            put("to", new String[]{to});
//            put("vocabId", new String[]{vocabId});
        }};
    }


}

