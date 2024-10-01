/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import com.wangxi.removewatermark.common.servicefacade.model.VideoDTO;

/**
 * 解析器
 *
 * @author wuxi
 * @version $Id: Parser.java, v 0.1 2024-05-21 14:24 wuxi Exp $$
 * @date 2024/05/21
 */
public interface Parser {
    /**
     * 解析视频
     *
     * @param originalUrl 原始地址
     */
    VideoDTO parseVideo(String originalUrl);
}