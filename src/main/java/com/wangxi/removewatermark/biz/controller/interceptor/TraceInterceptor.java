/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.biz.controller.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wangxi.removewatermark.common.utils.constants.LoggerConstants;

/**
 * 跟踪拦截器
 *
 * @author wuxi
 * @version $Id: TraceInterceptor.java, v 0.1 2024-06-12 17:56 wuxi Exp $$
 * @date 2024/06/12
 */
@Component
public class TraceInterceptor implements HandlerInterceptor {
    /**
     * 前处理
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(LoggerConstants.TRACE_ID, UUID.randomUUID().toString());
        return true;
    }

    /**
     * 完成后
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @param ex       异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {
        MDC.remove(LoggerConstants.TRACE_ID);
    }
}