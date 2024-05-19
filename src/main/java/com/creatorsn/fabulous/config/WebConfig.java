package com.creatorsn.fabulous.config;


import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.providers.ActuatorProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerResourceResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;
import java.util.Optional;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    private final String swaggerPath;
    private final SwaggerIndexTransformer swaggerIndexTransformer;
    private final Optional<ActuatorProvider> actuatorProvider;
    private final SwaggerResourceResolver swaggerResourceResolver;

    public WebConfig(SwaggerUiConfigParameters swaggerUiConfigParameters, SwaggerIndexTransformer swaggerIndexTransformer, Optional<ActuatorProvider> actuatorProvider, SwaggerResourceResolver swaggerResourceResolver) {
        this.swaggerPath = swaggerUiConfigParameters.getPath();
        this.swaggerIndexTransformer = swaggerIndexTransformer;
        this.actuatorProvider = actuatorProvider;
        this.swaggerResourceResolver = swaggerResourceResolver;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        StringBuilder uiRootPath = new StringBuilder();
        if (this.swaggerPath.contains("/")) {
            uiRootPath.append(this.swaggerPath, 0, this.swaggerPath.lastIndexOf("/"));
        }

        if (this.actuatorProvider.isPresent() && ((ActuatorProvider) this.actuatorProvider.get()).isUseManagementPort()) {
            uiRootPath.append(((ActuatorProvider) this.actuatorProvider.get()).getBasePath());
        }

        registry.addResourceHandler(new String[]{"" + uiRootPath + "/swagger-ui*/*swagger-initializer.js"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(0).resourceChain(false).addResolver(this.swaggerResourceResolver).addTransformer(this.swaggerIndexTransformer);
        registry.addResourceHandler(new String[]{"" + uiRootPath + "/swagger-ui*/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).resourceChain(false).addResolver(this.swaggerResourceResolver).addTransformer(this.swaggerIndexTransformer);
    }

    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 这个继承会使得swagger失效
        AntPathMatcher pathMatcher = new AntPathMatcher();
        pathMatcher.setCaseSensitive(false);
        configurer.setPathMatcher(pathMatcher);
    }

    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    }

    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }

    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    }

    public void addFormatters(FormatterRegistry registry) {
    }

    public void addInterceptors(InterceptorRegistry registry) {
    }

    public void addCorsMappings(CorsRegistry registry) {
    }

    public void addViewControllers(ViewControllerRegistry registry) {
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }

    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    }

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    }

    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    }

    @Nullable
    public Validator getValidator() {
        return null;
    }

    @Nullable
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}

