/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.dal.daointerface;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxi.removewatermark.common.dal.dataobject.UserInfoDO;

/**
 * 用户信息dao
 *
 * @author wuxi
 * @version $Id: UserInfoDAO.java, v 0.1 2024-05-21 14:06 wuxi Exp $$
 * @date 2024/05/21
 */
@Mapper
public interface UserInfoDAO extends BaseMapper<UserInfoDO> {

}