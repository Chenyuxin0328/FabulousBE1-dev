package com.creatorsn.fabulous.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 随机工具类
 */
public class RandomUtil {

    /**
     * 根据时间序列生成对应的随机序列，前17位为时间序列，后32位为UUID
     * @param width 宽度 17-49
     * @return 随机序列
     */
    public static String DateTimeRandomId(int width){
        if (width<17 || width>49){
            throw new IllegalArgumentException("width must be between 1 and 49");
        }
        var datetime = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        var uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return (datetime+uuid).substring(0, width);
    }

    /**
     * 生成一个32位的UUID
     * @return 32位的UUID
     */
    public static String DateTimeUUID(){
        var inputString = DateTimeRandomId(32);
        String[] parts = new String[5];
        parts[0] = inputString.substring(0, 8);
        parts[1] = inputString.substring(8, 12);
        parts[2] = inputString.substring(12, 16);
        parts[3] = inputString.substring(16, 20);
        parts[4] = inputString.substring(20);
        return String.join("-", parts);
    }
}
