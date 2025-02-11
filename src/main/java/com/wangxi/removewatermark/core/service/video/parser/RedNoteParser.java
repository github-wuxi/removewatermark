/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.servicefacade.enums.VideoSourceEnum;
import com.wangxi.removewatermark.common.servicefacade.model.VideoDTO;
import com.wangxi.removewatermark.common.utils.exception.BizException;

/**
 * 小红书解析器
 *
 * @author wuxi
 * @version $Id: KuaiShouParser.java, v 0.1 2024-05-21 14:23 wuxi Exp $$
 * @date 2024/05/21
 */
@Service
public class RedNoteParser extends AbstractParser {
    /**
     * 数据key
     */
    private static final String DATA_KEY = "window.__INITIAL_STATE__=";

    /**
     * 解析视频
     *
     * @param originalUrl 原始地址
     */
    @Override
    public VideoDTO parseVideo(String originalUrl) {
        // 1、去除掉无用视频前后缀
        String url = fetchTargetUrl(originalUrl, "(https?://xhslink.com/[\\w/]+[a-zA-Z0-9])");

        // 2、获取重定向后的地址
        HttpHeaders headForResponse = restTemplateService.headForHeaders(url);
        url = headForResponse.getLocation().toString();

        // 3、http调用获取页面信息
        HttpHeaders httpHeaders = fetchHttpHeaders(url, null);
        String content = restTemplateService.fetchHttpEntity(url, httpHeaders, HttpMethod.GET, null, String.class);
        if (StringUtils.isBlank(content)) {
            throw new BizException(ErrorCodeEnum.WEB_API_CALL_FAIL);
        }

        // 4、提取出对象
        Document document = Jsoup.parse(content);
        for (Element element : document.getAllElements()) {
            if (element == null) {
                continue;
            }
            int index = element.data().indexOf(DATA_KEY);
            if (index < 0) {
                continue;
            }
            // 解码
            String decodeContent = element.data().substring(index + DATA_KEY.length());
            DocumentContext context = JsonPath.parse(decodeContent);

            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setVideoId(context.read("$.noteData.data.noteData.noteId"));
            videoDTO.setVideoSource(VideoSourceEnum.RED_NOTE.getCode());
            videoDTO.setVideoOriginalUrl(originalUrl);
            videoDTO.setVideoTitle(context.read("$.noteData.data.noteData.title"));
            videoDTO.setVideoCover(context.read("$.noteData.data.noteData.imageList[0].url"));
            String videoParsedUrl;
            try {
                videoParsedUrl = context.read("$.noteData.data.noteData.video.media.stream.h266[0].masterUrl");
            } catch (PathNotFoundException p1) {
                try {
                    videoParsedUrl = context.read("$.noteData.data.noteData.video.media.stream.h265[0].masterUrl");
                } catch (PathNotFoundException p2) {
                    videoParsedUrl = context.read("$.noteData.data.noteData.video.media.stream.h264[0].masterUrl");
                }
            }
            videoDTO.setVideoParsedUrl(videoParsedUrl);
            return videoDTO;
        }
        return null;
    }
}