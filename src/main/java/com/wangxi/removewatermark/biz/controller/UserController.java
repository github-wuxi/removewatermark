/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.biz.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wangxi.removewatermark.common.dal.dataobject.UserInfoDO;
import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.servicefacade.model.UserLoginResult;
import com.wangxi.removewatermark.core.service.userinfo.UserInfoService;

/**
 * 用户控制器
 *
 * @author wuxi
 * @version $Id: UserController.java, v 0.1 2024-05-20 16:16 wuxi Exp $$
 */
@RestController
@RequestMapping("removewatermark/user")
public class UserController {
    /**
     * 用户服务
     */
    @Resource
    private UserInfoService userInfoService;

    /**
     * 登录
     *
     * @param code      临时登录凭证 code
     * @return {@link BaseResult}<{@link UserLoginResult}>
     */
    @RequestMapping(value = "/login.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult<UserLoginResult> login(String code) {
        return userInfoService.login(code);
    }

    /**
     * 激励
     *
     * @param userId    用户id
     * @param rewardNum 激励次数
     * @return {@link BaseResult}
     */
    @RequestMapping(value = "/reward.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult reward(String userId, int rewardNum) {
        return userInfoService.reward(userId, rewardNum);
    }

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return {@link BaseResult}<{@link UserInfoDO}>
     */
    @RequestMapping(value = "/queryUserInfo.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult<UserInfoDO> queryUserInfo(String userId) {
        return userInfoService.queryUserInfo(userId);
    }

    /**
     * 更新用户信息
     *
     * @param userId   用户id
     * @param userName 用户名
     * @return {@link BaseResult}
     */
    @RequestMapping(value = "/updateUserInfo.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult updateUserInfo(String userId, String userName) {
        return userInfoService.updateUserInfo(userId, userName);
    }


    /**
     * 上传用户头像
     *
     * @param userId          用户id
     * @param avatarUrlBase64 头像url base64编码的
     * @return {@link BaseResult}
     */
    @RequestMapping(value = "/uploadUserAvatar.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult<String> uploadUserAvatar(String userId, String avatarUrlBase64) {
        return userInfoService.uploadUserAvatar(userId, avatarUrlBase64);
    }
}