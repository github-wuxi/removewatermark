/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.common.lang.StringUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.wangxi.removewatermark.common.servicefacade.enums.VideoSourceEnum;
import com.wangxi.removewatermark.common.servicefacade.model.VideoDTO;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.utils.exception.BizException;

/**
 * 抖音解析器
 *
 * @author wuxi
 * @version $Id: DouYinParser.java, v 0.1 2024-05-21 14:22 wuxi Exp $$
 * @date 2024/05/21
 */
@Service
public class DouYinParser extends AbstractParser {
    /**
     * 解析视频
     *
     * @param originalUrl 原始地址
     */
    @Override
    public VideoDTO parseVideo(String originalUrl) {
        // 1、获取重定向后的地址，来获取视频id
        String url = restTemplate.headForHeaders(originalUrl).getLocation().toString();
        Matcher matcher = Pattern.compile("/share/video/([\\d]*)[/|?]").matcher(url);
        if (!matcher.find()) {
            throw new BizException(ErrorCodeEnum.ILLEGAL_VIDEO_URL);
        }
        String videoId = matcher.group(1);
        if (StringUtil.isBlank(videoId)) {
            throw new BizException(ErrorCodeEnum.ILLEGAL_VIDEO_URL);
        }

        // 2、http调用api获取结果
        String dyWebApi = "https://www.iesdouyin.com/share/video/" + videoId;
        HttpHeaders httpHeaders = fetchHttpHeaders(dyWebApi, null);
        String content = fetchHttpEntity(dyWebApi, httpHeaders, HttpMethod.GET, null, String.class);
        if (StringUtil.isBlank(content)) {
            throw new BizException(ErrorCodeEnum.WEB_API_CALL_FAIL);
        }

        // 3、解析出html标签，找到RENDER_DATA元素
        Document document = Jsoup.parse(content);
        Element element = document.getElementById("RENDER_DATA");
        // 解码
        String decodeContent = URLDecoder.decode(element.data());

        // 4、提取出对象
        DocumentContext context = JsonPath.parse(decodeContent);
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setVideoId(videoId);
        videoDTO.setVideoSource(VideoSourceEnum.DOU_YIN.getCode());
        videoDTO.setVideoOriginalUrl(originalUrl);
        videoDTO.setVideoTitle(context.read("$.app.videoInfoRes.item_list[0].desc"));
        videoDTO.setVideoCover(context.read("$.app.videoInfoRes.item_list[0].video.cover.url_list[0]"));
        videoDTO.setVideoParsedUrl(((String) context.read("$.app.videoInfoRes.item_list[0].video.play_addr.url_list[0]"))
            .replace("playwm", "play"));
        return videoDTO;
    }
}