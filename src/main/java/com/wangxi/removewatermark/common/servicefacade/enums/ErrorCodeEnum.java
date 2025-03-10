/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.common.servicefacade.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author wuxi
 * @version $Id: ErrorCodeEnum.java, v 0.1 2024-05-20 16:53 wuxi Exp $$
 * @date 2024/05/20
 */
@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION(false, "UNKNOWN_EXCEPTION", "未知异常"),

    /**
     * 非法参数
     */
    ILLEGAL_ARGUMENTS(false, "ILLEGAL_ARGUMENTS", "参数异常，请排查=%s"),

    /**
     * 无可用解析计数
     */
    NONE_AVAILABLE_PARSE_COUNT(false, "NONE_AVAILABLE_PARSE_COUNT", "无可用次数，完成广告观看可获取次数"),

    /**
     * 非法的视频链接
     */
    ILLEGAL_VIDEO_URL(false, "ILLEGAL_VIDEO_URL", "视频链接不正确，请更正后重试"),

    /**
     * Web API调用失败
     */
    WEB_API_CALL_FAIL(false, "WEB_API_CALL_FAIL", "解析失败，请重试或联系客服解决"),

    /**
     * 用户信息为空
     */
    USER_INFO_NULL(false, "USER_INFO_NULL", "用户信息查询为空，请登录后再重试"),

    /**
     * SFTP关闭连接失败
     */
    SFTP_DISCONNECT_FAIL(true, "SFTP_DISCONNECT_FAIL", "请重试"),

    /**
     * SFTP上传文件失败
     */
    SFTP_UPLOAD_FILE_FAIL(true, "SFTP_UPLOAD_FILE_FAIL", "请重试"),

    /**
     * 文件删除失败
     */
    FILE_DELETE_FAIL(true, "FILE_DELETE_FAIL", "请重试"),
    ;

    /**
     * 是否可重试失败
     */
    private final boolean retryFail;

    /**
     * 结果码
     */
    private final String resultCode;

    /**
     * 结果描述
     */
    private final String resultDesc;

    private static final Map<String, ErrorCodeEnum> codeMap = new HashMap<>();
    static {
        for (ErrorCodeEnum item : ErrorCodeEnum.values()) {
            codeMap.put(item.getResultCode(), item);
        }
    }

    /**
     * 按code获取枚举值
     *
     * @param code 代码
     * @return {@link ErrorCodeEnum}
     */
    public static ErrorCodeEnum getByCode(String code) {
        return codeMap.get(code);
    }
}