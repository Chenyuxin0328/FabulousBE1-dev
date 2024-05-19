package com.creatorsn.fabulous.util;

import com.creatorsn.fabulous.util.configuration.HashUtilConfiguration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashUtil {

    final HashUtilConfiguration hashUtilConfiguration;

    public HashUtil(HashUtilConfiguration hashUtilConfiguration) {
        this.hashUtilConfiguration = hashUtilConfiguration;
    }

    /**
     * 对值进行编码
     * @param value 值
     * @return 返回hash后的值
     */
    public String encode(String value){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] inputBytes = value.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = md.digest(inputBytes);
            StringBuilder builder = new StringBuilder();
            for (byte b:hashBytes){
                builder.append(String.format("%02x",b));
            }
            String md5Hash = builder.toString();
            String saltHash = md5Hash + this.hashUtilConfiguration.getSalt();
            MessageDigest md2 = MessageDigest.getInstance("SHA-256");
            inputBytes = saltHash.getBytes(StandardCharsets.UTF_8);
            hashBytes = md2.digest(inputBytes);
            builder = new StringBuilder();
            for (byte b: hashBytes){
                builder.append(String.format("%02x",b));
            }
            return builder.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证是否该值hash后是否和某个hash值相等
     * @param value 原值
     * @param hash hash后的值
     * @return 相等返回true，否则返回false
     */
    public boolean verify(String value,String hash){
        value = encode(value);
        return hash.equals(value);
    }
}
