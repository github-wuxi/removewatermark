/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.utils;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

import org.apache.commons.lang3.StringUtils;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.utils.exception.BizException;

/**
 * 断言工具
 *
 * @author wuxi
 * @version $Id: AssertUtil.java, v 0.1 2024-05-20 17:37 wuxi Exp $$
 * @date 2024/05/20
 */
public class AssertUtil {
    /**
     * 参数确认-非空
     *
     * @param obj      断言对象
     * @param errorMsg 错误描述
     * @param args     错误参数
     */
    public static void assertNotNull(Object obj, String errorMsg, Object... args) {
        if (obj == null) {
            bizIllegalArgumentException(errorMsg, args);
        }
    }

    /**
     * 断言不为空
     *
     * @param obj       对象
     * @param errorCode 错误代码
     * @param args      参数
     */
    public static void assertNotNull(Object obj, ErrorCodeEnum errorCode, Object... args) {
        if (obj == null) {
            throw new BizException(errorCode, args);
        }
    }

    /**
     * 断言字符串不为空, 为空抛出指定错误信息异常
     *
     * @param str      断言字符串
     * @param errorMsg 错误信息
     * @param args     错误参数
     */
    public static void assertNotBlank(String str, String errorMsg, Object... args) {
        if (StringUtils.isBlank(str)) {
            bizIllegalArgumentException(errorMsg, args);
        }
    }

    /**
     * 表达式判断, false抛出异常
     *
     * @param expression 需要判断的表达式
     * @param errorMsg   错误码
     */
    public static void assertTrue(Boolean expression, String errorMsg, Object... args) {
        if (!expression) {
            bizIllegalArgumentException(errorMsg, args);
        }
    }

    /**
     * 表达式判断, false抛出异常
     *
     * @param expression 需要判断的表达式
     * @param errorCode  错误码
     */
    public static void assertTrue(Boolean expression, ErrorCodeEnum errorCode, Object... args) {
        if (!expression) {
            throw new BizException(errorCode, args);
        }
    }

    /**
     * 集合非空断言
     *
     * @param collection 集合
     * @param errorMsg   错误描述
     */
    public static void assertNotEmpty(Collection<?> collection, String errorMsg, Object... args) {
        if (CollectionUtils.isEmpty(collection)) {
            bizIllegalArgumentException(errorMsg, args);
        }
    }

    /**
     * 最小值断言
     *
     * @param value    值
     * @param minValue 最小值
     * @param errorMsg 错误描述
     */
    public static void assertMin(int value, int minValue, String errorMsg, Object... args) {
        if (value < minValue) {
            bizIllegalArgumentException(errorMsg, args);
        }
    }

    /**
     * 最大值断言
     *
     * @param value    值
     * @param maxValue 最大值
     * @param errorMsg 错误描述
     */
    public static void assertMax(int value, int maxValue, String errorMsg, Object... args) {
        if (value > maxValue) {
            bizIllegalArgumentException(errorMsg, args);
        }
    }

    /**
     * 业务非法参数异常
     *
     * @param message 信息
     * @param args    参数
     */
    private static void bizIllegalArgumentException(String message, Object[] args) {
        throw new BizException(ErrorCodeEnum.ILLEGAL_ARGUMENTS, String.format(message, args));
    }
}