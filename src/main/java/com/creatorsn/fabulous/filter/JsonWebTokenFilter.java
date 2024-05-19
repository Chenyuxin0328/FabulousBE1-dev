package com.creatorsn.fabulous.filter;

import com.creatorsn.fabulous.util.JsonWebTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Json Web Token 认证授权过滤器
 */
@Component
public class JsonWebTokenFilter extends OncePerRequestFilter {
    /**
     * JsonWebUtil工具类
     */
    final private JsonWebTokenUtil jsonWebTokenUtil;

    /**
     * 认证的方案
     */
    final private String schema = "bearer";

    public JsonWebTokenFilter(JsonWebTokenUtil jsonWebTokenUtil) {
        this.jsonWebTokenUtil = jsonWebTokenUtil;
    }

    /**
     * 获取Header中的AccessToken，并判断格式是否正确
     *
     * @param request 请求
     * @return 如果正确，则返回JWT，否则返回null
     */
    private String getAccessToken(HttpServletRequest request) {
        var accessToken = request.getHeader("Authorization");
        if (StringUtils.hasText(accessToken)) {
            String[] token = accessToken.split(" ");
            if (token.length == 2) {
                if (token[0].equalsIgnoreCase(schema)) {
                    return token[1];
                }
            }
        } else {
            accessToken = request.getParameter("Authorization");
            if (StringUtils.hasText(accessToken)) {
                return accessToken;
            }
        }
        return null;
    }

    /**
     * @param request     HTTP请求
     * @param response    HTTP响应
     * @param filterChain 其他的Filter链
     * @throws ServletException 服务异常
     * @throws IOException      IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                // 获取用户名
                Claims claims = jsonWebTokenUtil.getTokenClaims(accessToken);
                String userId = claims.getSubject();
                // 获取所有的角色
                List<String> roles = (List<String>) claims.get("roles");
                List<SimpleGrantedAuthority> authorities = Objects.isNull(roles) ?
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_user")) :
                        roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(new User(userId, null, authorities)
//                                , accessToken, authorities);
                        new UsernamePasswordAuthenticationToken(userId, accessToken, authorities);
                // 存储于Spring上下文中
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception ex) {
                ex.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
