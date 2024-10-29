/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.model;

import lombok.Data;

/**
 * 用户登录结果
 *
 * @author wuxi
 * @version $Id: UserLoginResult.java, v 0.1 2024-10-03 15:22 wuxi Exp $$
 */
@Data
public class UserLoginResult {
    /**
     * 微信的开放id，用户标识
     */
    private String openId;

    /**
     * 今日首次登录文案
     */
    private String todayFirstTimeLoginText;
}