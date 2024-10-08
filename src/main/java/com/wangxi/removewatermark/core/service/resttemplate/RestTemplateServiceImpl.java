/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.resttemplate;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * http请求服务实现
 *
 * @author wuxi
 * @version $Id: RestTemplateServiceImpl.java, v 0.1 2024-10-02 15:39 wuxi Exp $$
 */
@Service
public class RestTemplateServiceImpl implements RestTemplateService {
    /**
     * http网络请求服务
     */
    @Resource
    private RestTemplate restTemplate;

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
    @Override
    public <T> T fetchHttpEntity(String url, HttpHeaders httpHeaders, HttpMethod httpMethod,
                                 Map<String, Object> params, Class<T> responseType) {
        HttpEntity<?> httpEntity = new HttpEntity<>(params, httpHeaders);
        return restTemplate.exchange(url, httpMethod, httpEntity, responseType).getBody();
    }

    /**
     * 使用HEAD获取请求头
     *
     * @param url url
     * @return {@link HttpHeaders}
     */
    @Override
    public HttpHeaders headForHeaders(String url) {
        return restTemplate.headForHeaders(url);
    }
}