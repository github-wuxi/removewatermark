/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.file;

/**
 * 文件服务
 *
 * @author wuxi
 * @version $Id: FileService.java, v 0.1 2024-10-02 15:34 wuxi Exp $$
 */
public interface FileService {
    /**
     * sftp上传
     *
     * @param fileBytes 文件字节
     * @param fileName  文件名称
     * @param filePath  文件路径
     */
    void sftpUpload(byte[] fileBytes, String fileName, String filePath);
}