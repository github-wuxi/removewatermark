/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.template;

import java.util.List;

/**
 * 服务回调
 *
 * @author wuxi
 * @version $Id: ServiceCallback.java, v 0.1 2024-05-20 16:35 wuxi Exp $$
 * @date 2024/05/20
 */
public interface ServiceCallback {
    /**
     * 前置参数检查
     */
    void checkParameter();

    /**
     * 执行服务处理
     */
    void process();

    /**
     * 日志打印
     */
    void finalLog();

    /**
     * 需要额外摘要打印的元素列表
     */
    List<String> extraDigestLogItemList();
}