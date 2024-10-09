/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.biz.controller.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author wuxi
 * @version $Id: InterceptorConfig.java, v 0.1 2024-10-09 15:15 wuxi Exp $$
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public TraceInterceptor initTraceInterceptor() {
        return new TraceInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initTraceInterceptor()).addPathPatterns("/**");
    }
}