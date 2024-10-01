/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.common.lang.StringUtil;
import com.wangxi.removewatermark.common.utils.constants.CharsetConstants;

/**
 * 解析器抽象类
 *
 * @author wuxi
 * @version $Id: AbstractParser.java, v 0.1 2024-05-21 14:38 wuxi Exp $$
 * @date 2024/05/21
 */
public abstract class AbstractParser implements Parser {
    /**
     * http网络请求服务
     */
    @Resource
    protected RestTemplate restTemplate;

    /**
     * 将set cookie转换为cookie
     *
     * @param httpHeaders http头信息
     * @return {@link String}
     */
    protected String convertSetCookieToCookie(HttpHeaders httpHeaders) {
        List<String> setCookieList = httpHeaders.get(HttpHeaders.SET_COOKIE);
        if (CollectionUtils.isEmpty(setCookieList)) {
            return StringUtil.EMPTY_STRING;
        }
        StringBuilder cookie = new StringBuilder();
        for (String setCookie : setCookieList) {
            int separatorIndex = setCookie.indexOf(CharsetConstants.SEMICOLON_WITH_SPACE);
            if (separatorIndex >= 0) {
                String cookieKeyAndValue = setCookie.substring(0, separatorIndex);
                cookie.append(cookieKeyAndValue);
                cookie.append(CharsetConstants.SEMICOLON_WITH_SPACE);
            }
        }
        return cookie.substring(0, cookie.length() - CharsetConstants.SEMICOLON_WITH_SPACE.length());
    }

    /**
     * 获取HTTP报头
     *
     * @param url url
     * @return {@link HttpHeaders}
     */
    protected HttpHeaders fetchHttpHeaders(String url, String cookie) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T)" +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Mobile Safari/537.36");
        httpHeaders.set(HttpHeaders.REFERER, url);
        if (StringUtil.isNotBlank(cookie)) {
            httpHeaders.set(HttpHeaders.COOKIE, cookie);
        }
        return httpHeaders;
    }

    /**
     * 获取HTTP实体
     *
     * @param url          url
     * @param httpMethod   http方法
     * @param params       参数
     * @param responseType 响应类型
     * @return {@link T}
     */
    protected <T> T fetchHttpEntity(String url, HttpHeaders httpHeaders,
                                    HttpMethod httpMethod, Map<String, Object> params, Class<T> responseType) {
        HttpEntity<?> httpEntity = new HttpEntity<>(params, httpHeaders);
        return restTemplate.exchange(url, httpMethod, httpEntity, responseType).getBody();
    }
}