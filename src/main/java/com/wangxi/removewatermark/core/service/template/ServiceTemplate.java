/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import org.apache.commons.lang3.StringUtils;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.utils.MethodUtil;
import com.wangxi.removewatermark.common.utils.constants.CharsetConstants;
import com.wangxi.removewatermark.common.utils.constants.LoggerConstants;
import com.wangxi.removewatermark.common.utils.exception.BizException;

/**
 * 服务模板
 *
 * @author wuxi
 * @version $Id: ServiceTemplate.java, v 0.1 2024-05-20 16:35 wuxi Exp $$
 * @date 2024/05/20
 */
public class ServiceTemplate {
    /**
     * 摘要日志记录器
     */
    private static final Logger DIGEST_LOGGER = LoggerFactory.getLogger(LoggerConstants.CORE_SERVICE_DIGEST_LOGGER);

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTemplate.class);

    /**
     * 执行
     *
     * @param result   结果
     * @param callback 回调
     */
    public static void execute(BaseResult result, ServiceCallback callback) {
        // 获取调用方法名
        String declareMethod = MethodUtil.getPureCallMethodName(ServiceTemplate.class);

        // 计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            // 执行服务校验参数
            callback.checkParameter();

            // 执行服务处理
            callback.process();

            result.setSuccess(true);
        } catch (BizException e) {
            LOGGER.warn(String.format("BizException, (method=%s, errorCode=%s, message=%s)", declareMethod, e.getResultCode(), e.getMessage()), e);
            BaseResult.fillFailureResult(result, ErrorCodeEnum.getByCode(e.getResultCode()), e.getMessage());
        } catch (Throwable e) {
            LOGGER.error(String.format("Throwable, (method=%s, message=%s)", declareMethod, e.getMessage()), e);
            BaseResult.fillFailureResult(result, ErrorCodeEnum.UNKNOWN_EXCEPTION);
        } finally {
            // 执行服务打印
            callback.finalLog();

            // 摘要日志处理
            stopWatch.stop();
            String extraDigestStr = StringUtils.EMPTY;
            List<String> extraDigestLogItemList = callback.extraDigestLogItemList();
            if (!CollectionUtils.isEmpty(extraDigestLogItemList)) {
                extraDigestStr = String.join(CharsetConstants.COMMA, extraDigestLogItemList);
            }
            // 摘要打印：方法名、耗时、是否成功、是否可重试、结果码、额外摘要
            DIGEST_LOGGER.info(String.format("%s|%s|%s|%s|%s|%s", declareMethod, stopWatch.getTotalTimeMillis(),
                result.isSuccess(), result.getErrorCode() == null ? StringUtils.EMPTY : result.isRetryFail(),
                result.getErrorCode() == null ? StringUtils.EMPTY : result.getErrorCode(), extraDigestStr));
        }
    }
}