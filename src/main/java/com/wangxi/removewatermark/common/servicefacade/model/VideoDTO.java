/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.model;

import lombok.Data;

/**
 * 视频dto
 *
 * @author wuxi
 * @version $Id: VideoDTO.java, v 0.1 2024-05-21 14:27 wuxi Exp $$
 * @date 2024/05/21
 */
@Data
public class VideoDTO {
    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频来源
     */
    private String videoSource;

    /**
     * 视频标题
     */
    private String videoTitle;

    /**
     * 视频封面
     */
    private String videoCover;

    /**
     * 视频解析后地址
     */
    private String videoParsedUrl;

    /**
     * 视频原始地址
     */
    private String videoOriginalUrl;
}