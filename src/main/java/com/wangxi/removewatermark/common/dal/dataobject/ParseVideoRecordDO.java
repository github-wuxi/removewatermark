/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.dal.dataobject;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 解析视频记录do
 *
 * @author wuxi
 * @version $Id: ParseVideoRecordDO.java, v 0.1 2024-05-21 13:48 wuxi Exp $$
 * @date 2024/05/21
 */
@TableName("parse_video_record")
@Data
public class ParseVideoRecordDO {
    /**
     * 主键Id
     */
    private long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 用户id
     */
    private String userId;

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