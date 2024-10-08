/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.userinfo;

import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.core.model.UserLoginResult;

/**
 * 用户服务
 *
 * @author wuxi
 * @version $Id: UserInfoService.java, v 0.1 2024-10-01 17:16 wuxi Exp $$
 */
public interface UserInfoService {
    /**
     * 登录
     *
     * @param code     临时登录凭证 code
     * @param nickName 昵称
     * @return {@link BaseResult}<{@link String}>
     */
    BaseResult<UserLoginResult> login(String code, String nickName);
}