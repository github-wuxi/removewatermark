/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.utils.exception;

import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.utils.constants.CharsetConstants;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author wuxi
 * @version $Id: BizException.java, v 0.1 2024-05-20 17:32 wuxi Exp $$
 * @date 2024/05/20
 */
@Getter
public class BizException extends RuntimeException {
    /**
     * 是否可重试失败
     */
    private boolean retryFail;

    /**
     * 结果码
     */
    private String resultCode;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @param argArray  参数数组
     */
    public BizException(ErrorCodeEnum errorCode, final Object... argArray) {
        super(wrapMessage(errorCode, argArray), null, false, false);
        if (errorCode == null) {
            return;
        }
        this.retryFail = errorCode.isRetryFail();
        this.resultCode = errorCode.getResultCode();
        this.resultDesc = errorCode.getResultDesc();
    }

    /**
     * 包装信息
     *
     * @param errorCode 错误代码
     * @param arguments 参数
     * @return {@link String}
     */
    private static String wrapMessage(ErrorCodeEnum errorCode, Object[] arguments) {
        if (errorCode == null) {
            return null;
        }
        String errorCodeStr = CharsetConstants.LEFT_SQUARE_BRACKET + errorCode.getResultCode()
            + CharsetConstants.RIGHT_SQUARE_BRACKET;
        if (arguments == null || arguments.length == 0) {
            return errorCodeStr + errorCode.getResultDesc();
        } else {
            return errorCodeStr + String.format(errorCode.getResultDesc(), arguments);
        }
    }
}