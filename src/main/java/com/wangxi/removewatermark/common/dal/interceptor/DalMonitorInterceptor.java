/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.dal.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.wangxi.removewatermark.common.utils.constants.CharsetConstants;
import com.wangxi.removewatermark.common.utils.constants.LoggerConstants;

/**
 * 数据库监控拦截器
 *
 * @author wuxi
 * @version $Id: DalMonitorInterceptor.java, v 0.1 2024-06-12 17:02 wuxi Exp $$
 * @date 2024/06/12
 */
@Component
public class DalMonitorInterceptor implements MethodInterceptor {
    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerConstants.COMMON_DAL_DIGEST_LOGGER);

    /**
     * 调用
     *
     * @param invocation 调用
     * @return {@link Object}
     * @throws Throwable throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String className = invocation.getMethod().getDeclaringClass().getSimpleName();
        String method = className + CharsetConstants.DOT + invocation.getMethod().getName();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean success = true;
        try {
            return invocation.proceed();
        } catch (Throwable ex) {
            success = false;
            throw ex;
        } finally {
            stopWatch.stop();
            LOGGER.info(String.format("(%s,%s,%s)", method, success, stopWatch.getTotalTimeMillis()));
        }
    }
}