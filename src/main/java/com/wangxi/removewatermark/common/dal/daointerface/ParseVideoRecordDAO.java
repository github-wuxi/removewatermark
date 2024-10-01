/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.dal.daointerface;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxi.removewatermark.common.dal.dataobject.ParseVideoRecordDO;

/**
 * 解析视频记录dao
 *
 * @author wuxi
 * @version $Id: ParseVideoRecordDAO.java, v 0.1 2024-05-21 14:03 wuxi Exp $$
 * @date 2024/05/21
 */
@Mapper
public interface ParseVideoRecordDAO extends BaseMapper<ParseVideoRecordDO> {

}