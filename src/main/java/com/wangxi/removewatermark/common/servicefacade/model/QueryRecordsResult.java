/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.model;

import java.util.List;

import lombok.Data;

/**
 * 查询记录请求
 *
 * @author wuxi
 * @version $Id: QueryRecordsRequest.java, v 0.1 2024-10-28 17:32 wuxi Exp $$
 */
@Data
public class QueryRecordsResult {
    /**
     * 视频列表
     */
    List<VideoDTO> videoList;

    /**
     * 是否有下一页，true代表是
     */
    boolean hasNext;
}