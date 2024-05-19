package com.creatorsn.fabulous.util;

import com.creatorsn.fabulous.util.configuration.JsonWebTokenConfiguration;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Component
public class JsonWebTokenUtil {
    final private JsonWebTokenConfiguration jsonWebTokenConfiguration;

    public JsonWebTokenUtil(JsonWebTokenConfiguration jsonWebTokenConfiguration) {
        this.jsonWebTokenConfiguration = jsonWebTokenConfiguration;
    }

    /**
     * 获取签名后的私钥
     * @return 获取签名后的私钥
     */
    private Key getSignedKey(){
        return Keys.hmacShaKeyFor(jsonWebTokenConfiguration.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建一个新的JSON WEB TOKEN
     * @param subject 主题
     * @param claims 一系列的声明
     * @param expiredMinutes 过期的分钟
     * @return 返回Token字符串
     */
    public String createToken(String subject, HashMap<String, Object> claims, long expiredMinutes){
        try {
            return Jwts.builder()
                    .setSubject(subject)
                    .setIssuer(jsonWebTokenConfiguration.getIssuer())
                    .addClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiredMinutes * 60 * 1000))
                    .signWith(getSignedKey(),SignatureAlgorithm.HS256)
                    .compact();
        }catch (InvalidKeyException exception){
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 解码Token，获取Token里的声明
     * @param token Token
     * @return 返回JSON WEB TOKEN解码后的Claims
     */
    public Claims getTokenClaims(String token){
        JwtParserBuilder builder = Jwts.parserBuilder();
        JwtParser parser =  builder.setSigningKey(getSignedKey()).build();
        try {
            return parser.parseClaimsJws(token).getBody();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 验证Token是否合法
     * @param token Token
     * @return 如果没有过期或者发布人正确则返回true,否则返回false
     */
    public boolean verify(String token){
        Claims claims = getTokenClaims(token);
        if (claims==null) return false;
        if (claims.getExpiration().before(new Date())) return false;
        return claims.getIssuer().equals(jsonWebTokenConfiguration.getIssuer());
    }
}
