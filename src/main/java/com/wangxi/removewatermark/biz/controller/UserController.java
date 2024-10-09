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

import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.core.model.UserLoginResult;
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
     * @param code     临时登录凭证 code
     * @param nickName 昵称
     * @return {@link BaseResult}<{@link UserLoginResult}>
     */
    @RequestMapping(value = "/login.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult<UserLoginResult> login(String code, String nickName) {
        return userInfoService.login(code, nickName);
    }
}