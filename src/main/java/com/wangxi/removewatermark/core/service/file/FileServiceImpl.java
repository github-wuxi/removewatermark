/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.file;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.utils.exception.BizException;
import com.wangxi.removewatermark.core.model.FileConfig;

/**
 * 文件服务
 *
 * @author wuxi
 * @version $Id: FileService.java, v 0.1 2024-10-02 15:34 wuxi Exp $$
 */
@Service
public class FileServiceImpl implements FileService {
    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * sftp上传
     *
     * @param fileBytes 文件字节
     * @param fileName  文件名称
     * @param filePath  文件路径
     */
    @Override
    public void sftpUpload(byte[] fileBytes, String fileName, String filePath) {
        Session session = null;
        Channel channel = null;
        OutputStream outputStream = null;
        // 超时1秒
        int connectTimeoutMs = 1000;
        try {
            session = (new JSch()).getSession(FileConfig.getUserName(), FileConfig.getHost());
            session.setPassword(FileConfig.getPassword());
            // 设置为不验证密钥
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(connectTimeoutMs);
            channel = session.openChannel("sftp");
            channel.connect(connectTimeoutMs);
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(filePath);
            outputStream = sftp.put(fileName);
            outputStream.write(fileBytes);
        } catch (Exception e) {
            LOGGER.error(String.format("sftpUpload(fileName:%s)，出现异常", fileName), e);
            throw new BizException(ErrorCodeEnum.SFTP_UPLOAD_FILE_FAIL);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (channel != null) {
                    channel.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            } catch (Exception e) {
                LOGGER.error(String.format("sftpUpload关闭连接(fileName:%s)，出现异常", fileName), e);
                throw new BizException(ErrorCodeEnum.SFTP_DISCONNECT_FAIL);
            }
        }
    }
}