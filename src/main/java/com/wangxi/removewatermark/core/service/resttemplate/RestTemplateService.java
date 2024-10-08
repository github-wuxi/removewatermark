/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.resttemplate;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * http请求服务
 *
 * @author wuxi
 * @version $Id: RestTemplateService.java, v 0.1 2024-10-02 15:34 wuxi Exp $$
 */
public interface RestTemplateService {
    /**
     * 获取HTTP实体
     *
     * @param url          url
     * @param httpHeaders  http头信息
     * @param httpMethod   http方法
     * @param params       参数
     * @param responseType 响应类型
     * @return {@link T}
     */
    <T> T fetchHttpEntity(String url, HttpHeaders httpHeaders, HttpMethod httpMethod,
                          Map<String, Object> params, Class<T> responseType);

    /**
     * 使用HEAD获取请求头
     *
     * @param url url
     * @return {@link HttpHeaders}
     */
    HttpHeaders headForHeaders(String url);
}