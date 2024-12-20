/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.userinfo;

import com.wangxi.removewatermark.common.dal.dataobject.UserInfoDO;
import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.servicefacade.model.UserLoginResult;

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
     * @param code      临时登录凭证 code
     * @return {@link BaseResult}<{@link UserLoginResult}>
     */
    BaseResult<UserLoginResult> login(String code);

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return {@link BaseResult}<{@link UserInfoDO}>
     */
    BaseResult<UserInfoDO> queryUserInfo(String userId);

    /**
     * 更新用户信息
     *
     * @param userId     用户id
     * @param userName   用户名
     * @return {@link BaseResult}
     */
    BaseResult updateUserInfo(String userId, String userName);

    /**
     * 上传用户头像
     *
     * @param userId          用户id
     * @param avatarUrlBase64 头像url base64
     * @return {@link BaseResult}<{@link String}>
     */
    BaseResult<String> uploadUserAvatar(String userId, String avatarUrlBase64);
}