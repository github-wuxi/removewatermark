/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 小程序配置
 *
 * @author wuxi
 * @version $Id: WechatConfig.java, v 0.1 2024-10-02 15:10 wuxi Exp $$
 */
@Configuration
public class WechatConfig {
    /**
     * 小程序id
     */
    private static String appId;

    /**
     * 密钥
     */
    private static String secret;

    /**
     * 用户签到的增加次数
     */
    private static long userSignInAddNumber;

    @Value("${project.wechat.app-id}")
    private void setAppId(String appId) {
        WechatConfig.appId = appId;
    }

    @Value("${project.wechat.secret}")
    private void setSecret(String secret) {
        WechatConfig.secret = secret;
    }

    @Value("${project.wechat.user-sign-in-add-number}")
    private void setUserSignInAddNumber(long userSignInAddNumber) {
        WechatConfig.userSignInAddNumber = userSignInAddNumber;
    }

    /**
     * Getter method for property <tt>appId</tt>.
     *
     * @return property value of appId
     */
    public static String getAppId() {
        return appId;
    }

    /**
     * Getter method for property <tt>secret</tt>.
     *
     * @return property value of secret
     */
    public static String getSecret() {
        return secret;
    }

    /**
     * Getter method for property <tt>userSignInAddNumber</tt>.
     *
     * @return property value of userSignInAddNumber
     */
    public static long getUserSignInAddNumber() {
        return userSignInAddNumber;
    }
}