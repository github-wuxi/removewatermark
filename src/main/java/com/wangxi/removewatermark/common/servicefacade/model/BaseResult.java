/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.model;

import org.slf4j.MDC;

import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.utils.constants.LoggerConstants;

import lombok.Data;

/**
 * 基本结果
 *
 * @author wuxi
 * @version $Id: BaseResult.java, v 0.1 2024-05-20 16:40 wuxi Exp $$
 * @date 2024/05/20
 */
@Data
public class BaseResult<T> {
    /**
     * 成功
     */
    private boolean success = true;

    /**
     * 错误码
     */
    private ErrorCodeEnum errorCode;

    /**
     * 结果数据
     */
    private T resultData;

    /**
     * 跟踪id
     */
    private String traceId;

    /**
     * 构造方法
     *
     * @param success    成功
     * @param errorCode  错误代码
     * @param resultData 结果数据
     */
    public BaseResult(boolean success, ErrorCodeEnum errorCode, T resultData) {
        this.success = success;
        this.errorCode = errorCode;
        this.resultData = resultData;
        traceId = MDC.get(LoggerConstants.TRACE_ID);
    }

    /**
     * 构造方法
     */
    public BaseResult() {
        traceId = MDC.get(LoggerConstants.TRACE_ID);
    }
}