/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.model;

import lombok.Data;

/**
 * 用户业务信息
 *
 * @author wuxi
 * @version $Id: UserBizInfo.java, v 0.1 2024-10-17 16:49 wuxi Exp $$
 */
@Data
public class UserBizInfo {
    /**
     * 是否为vip
     */
    private boolean whetherVip;

    /**
     * 剩余可用次数
     */
    private long availableNumber;

    /**
     * 已解析次数
     */
    private long parsedNumber;
}