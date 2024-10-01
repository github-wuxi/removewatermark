/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.utils;

import com.alibaba.common.lang.StringUtil;
import com.wangxi.removewatermark.common.utils.constants.CharsetConstants;

/**
 * 方法工具
 *
 * @author wuxi
 * @version $Id: MethodUtil.java, v 0.1 2024-05-20 17:16 wuxi Exp $$
 * @date 2024/05/20
 */
public class MethodUtil {
    /**
     * 获取调用类的方法
     *
     * @return “类”或“类.方法”的字符串
     */
    public static String getCallMethod(Class clz) {
        StringBuilder sb = new StringBuilder();

        String className = StringUtil.EMPTY_STRING;
        String methodName = StringUtil.EMPTY_STRING;

        // 获取调用堆栈
        StackTraceElement[] elements = new Throwable().getStackTrace();

        // 遍历当前堆栈，往上推一层即为实际调用方法
        if (elements != null && elements.length > 0) {
            for (int i = 0; i < elements.length; i++) {
                if (StringUtil.equals(clz.getName(), elements[i].getClassName())) {
                    if (i + 1 < elements.length) {
                        className = elements[i + 1].getClassName();
                        methodName = elements[i + 1].getMethodName();
                    }
                    break;
                }
            }
        }

        // 拼接成“类.方法”字符串返回
        if (StringUtil.isNotBlank(className)) {
            sb.append(className);
        }
        if (StringUtil.isNotBlank(methodName)) {
            sb.append(CharsetConstants.DOT).append(methodName);
        }

        return sb.toString();
    }

    /**
     * 得到调用的纯方法名，不包含类名
     *
     * @param clz clz
     * @return {@link String}
     */
    public static String getPureCallMethodName(Class clz) {
        String methodName = StringUtil.EMPTY_STRING;
        //获取调用堆栈
        StackTraceElement[] elements = new Throwable().getStackTrace();
        //遍历当前堆栈，往上推一层即为实际调用方法
        if (elements != null && elements.length > 0) {
            for (int i = 0; i < elements.length; i++) {
                if (StringUtil.equals(clz.getName(), elements[i].getClassName())) {
                    if (i + 1 < elements.length) {
                        methodName = elements[i + 1].getMethodName();
                    }
                    break;
                }
            }
        }
        return methodName;
    }
}