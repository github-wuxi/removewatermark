/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.dal.dataobject;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户信息do
 *
 * @author wuxi
 * @version $Id: UserInfoDO.java, v 0.1 2024-05-21 13:58 wuxi Exp $$
 * @date 2024/05/21
 */
@TableName("user_info")
@Data
public class UserInfoDO {
    /**
     * 主键Id
     */
    private long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 最新签到时间
     */
    private Date latestSignInTime;

    /**
     * 总签到次数
     */
    private long totalSignInNumber;

    /**
     * 剩余可用次数
     */
    private long availableNumber;

    /**
     * 已解析次数
     */
    private long parsedNumber;
}