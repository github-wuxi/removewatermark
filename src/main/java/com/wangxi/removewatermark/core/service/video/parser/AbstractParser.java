/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import com.wangxi.removewatermark.common.utils.constants.CharsetConstants;
import com.wangxi.removewatermark.core.service.resttemplate.RestTemplateService;

/**
 * 解析器抽象类
 *
 * @author wuxi
 * @version $Id: AbstractParser.java, v 0.1 2024-05-21 14:38 wuxi Exp $$
 * @date 2024/05/21
 */
public abstract class AbstractParser implements Parser {
    /**
     * http请求服务
     */
    @Resource
    protected RestTemplateService restTemplateService;

    /**
     * 将set cookie转换为cookie
     *
     * @param httpHeaders http头信息
     * @return {@link String}
     */
    protected String convertSetCookieToCookie(HttpHeaders httpHeaders) {
        List<String> setCookieList = httpHeaders.get(HttpHeaders.SET_COOKIE);
        if (CollectionUtils.isEmpty(setCookieList)) {
            return StringUtils.EMPTY;
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
        if (StringUtils.isNotBlank(cookie)) {
            httpHeaders.set(HttpHeaders.COOKIE, cookie);
        }
        return httpHeaders;
    }
}