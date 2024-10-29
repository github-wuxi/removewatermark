/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video;

import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.servicefacade.model.QueryRecordsRequest;
import com.wangxi.removewatermark.common.servicefacade.model.QueryRecordsResult;

/**
 * 视频服务
 *
 * @author wuxi
 * @version $Id: VideoService.java, v 0.1 2024-05-21 14:11 wuxi Exp $$
 * @date 2024/05/21
 */
public interface VideoService {
    /**
     * 解析视频
     *
     * @param url    url
     * @param userId 用户id
     * @return {@link BaseResult}<{@link Boolean}>
     */
    BaseResult parseVideo(String url, String userId);

    /**
     * 查询记录
     *
     * @param request 请求
     * @return {@link BaseResult}<{@link QueryRecordsResult}>
     */
    BaseResult<QueryRecordsResult> queryRecords(QueryRecordsRequest request);
}