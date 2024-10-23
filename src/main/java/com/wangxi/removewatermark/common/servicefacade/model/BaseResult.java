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
     * 是否可重试失败，true代表是
     */
    private boolean retryFail;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误描述信息
     */
    private String errorMsg;

    /**
     * 结果数据
     */
    private T resultData;

    /**
     * 跟踪id
     */
    private String traceId;

    /**
     * 填充失败结果
     *
     * @param resultData    结果数据
     * @param errorCodeEnum 错误码枚举
     */
    public static void fillFailureResult(BaseResult resultData, ErrorCodeEnum errorCodeEnum) {
        resultData.setSuccess(false);
        if (errorCodeEnum != null) {
            resultData.setRetryFail(errorCodeEnum.isRetryFail());
            resultData.setErrorCode(errorCodeEnum.getResultCode());
            resultData.setErrorMsg(errorCodeEnum.getResultDesc());
        }
        resultData.setTraceId(MDC.get(LoggerConstants.TRACE_ID));
    }

    /**
     * 构造方法
     */
    public BaseResult() {
        traceId = MDC.get(LoggerConstants.TRACE_ID);
    }
}