/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.enums;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频源
 *
 * @author wuxi
 * @version $Id: VideoSourceEnum.java, v 0.1 2024-05-21 16:36 wuxi Exp $$
 * @date 2024/05/21
 */
@Getter
@AllArgsConstructor
public enum VideoSourceEnum {
    /**
     * 抖音
     */
    DOU_YIN("DOU_YIN", "抖音", "douyin", "douYinParser"),

    /**
     * 快手
     */
    KUAI_SHOU("KUAI_SHOU", "快手", "kuaishou", "kuaiShouParser"),
    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * url关键词
     */
    private final String urlKeywords;

    /**
     * 解析器的bean id
     */
    private final String parserBeanId;

    private static final Map<String, VideoSourceEnum> codeMap = new HashMap<>();
    static {
        for (VideoSourceEnum item : VideoSourceEnum.values()) {
            codeMap.put(item.getCode(), item);
        }
    }

    /**
     * 按code获取枚举值
     *
     * @param code 代码
     * @return {@link VideoSourceEnum}
     */
    public static VideoSourceEnum getByCode(String code) {
        return codeMap.get(code);
    }

    /**
     * 通过url获取枚举值
     *
     * @param url url
     * @return {@link VideoSourceEnum}
     */
    public static VideoSourceEnum getByUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        for (VideoSourceEnum item : VideoSourceEnum.values()) {
            if (url.contains(item.getUrlKeywords())) {
                return item;
            }
        }
        return null;
    }
}