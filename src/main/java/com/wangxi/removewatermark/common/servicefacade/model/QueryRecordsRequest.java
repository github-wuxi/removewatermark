/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.model;

import lombok.Data;

/**
 * 查询记录请求
 *
 * @author wuxi
 * @version $Id: QueryRecordsRequest.java, v 0.1 2024-10-28 17:32 wuxi Exp $$
 */
@Data
public class QueryRecordsRequest {
    /**
     * 页码，从1开始
     */
    private int pageNum;

    /**
     * 页大小
     */
    private int pageSize = 10;

    /**
     * 用户id
     */
    private String userId;
}