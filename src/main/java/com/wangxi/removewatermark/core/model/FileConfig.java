/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 文件配置
 *
 * @author wuxi
 * @version $Id: FileConfig.java, v 0.1 2024-10-02 15:10 wuxi Exp $$
 */
@Component
public class FileConfig {
    /**
     * 域名地址
     */
    private static String host;

    /**
     * 用户名
     */
    private static String userName;

    /**
     * 密码
     */
    private static String password;

    /**
     * 头像文件路径
     */
    private static String avatarFilePath;

    /**
     * 头像文件前缀
     */
    private static String avatarFilePrefix;

    @Value("${project.file.host}")
    public static void setHost(String host) {
        FileConfig.host = host;
    }

    @Value("${project.file.user-name}")
    public static void setUserName(String userName) {
        FileConfig.userName = userName;
    }

    @Value("${project.file.password}")
    public static void setPassword(String password) {
        FileConfig.password = password;
    }

    @Value("${project.file.avatar-file-path}")
    public static void setAvatarFilePath(String avatarFilePath) {
        FileConfig.avatarFilePath = avatarFilePath;
    }

    @Value("${project.file.avatar-file-prefix}")
    public static void setAvatarFilePrefix(String avatarFilePrefix) {
        FileConfig.avatarFilePrefix = avatarFilePrefix;
    }

    /**
     * Getter method for property <tt>host</tt>.
     *
     * @return property value of host
     */
    public static String getHost() {
        return host;
    }

    /**
     * Getter method for property <tt>userName</tt>.
     *
     * @return property value of userName
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Getter method for property <tt>password</tt>.
     *
     * @return property value of password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Getter method for property <tt>avatarFilePath</tt>.
     *
     * @return property value of avatarFilePath
     */
    public static String getAvatarFilePath() {
        return avatarFilePath;
    }

    /**
     * Getter method for property <tt>avatarFilePrefix</tt>.
     *
     * @return property value of avatarFilePrefix
     */
    public static String getAvatarFilePrefix() {
        return avatarFilePrefix;
    }
}