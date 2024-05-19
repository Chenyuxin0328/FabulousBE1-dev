package com.creatorsn.fabulous.config;

import com.creatorsn.fabulous.filter.JsonWebTokenFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    final private JsonWebTokenFilter jsonWebTokenFilter;

    public JwtConfigurer(JsonWebTokenFilter jsonWebTokenFilter) {
        this.jsonWebTokenFilter = jsonWebTokenFilter;
    }

    @Override
    public void configure(HttpSecurity http){
        http.addFilterBefore(jsonWebTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
