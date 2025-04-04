/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.servicefacade.enums.VideoSourceEnum;
import com.wangxi.removewatermark.common.servicefacade.model.VideoDTO;
import com.wangxi.removewatermark.common.utils.exception.BizException;

/**
 * 快手解析器
 *
 * @author wuxi
 * @version $Id: KuaiShouParser.java, v 0.1 2024-05-21 14:23 wuxi Exp $$
 * @date 2024/05/21
 */
@Service
public class KuaiShouParser extends AbstractParser {
    /**
     * 数据前置key
     */
    private static final String DATA_PRE_KEY = "window.INIT_STATE = ";

    /**
     * 数据后置key
     */
    private static final String DATA_POST_KEY = "window.__USE_SSR__";

    /**
     * 解析视频
     *
     * @param originalUrl 原始地址
     */
    @Override
    public VideoDTO parseVideo(String originalUrl) {
        // 1、去除掉无用视频前后缀
        String url = fetchTargetUrl(originalUrl, "(https?://v.kuaishou.com/[\\S]*)");

        // 2、获取重定向后的地址，来获取视频id
        HttpHeaders headForResponse = restTemplateService.headForHeaders(url);
        url = headForResponse.getLocation().toString();
        // videoId解析
        Matcher videoIdmatcher = Pattern.compile("photoId=([^&]+)").matcher(url);
        if (!videoIdmatcher.find()) {
            throw new BizException(ErrorCodeEnum.WEB_API_CALL_FAIL);
        }
        String videoId = videoIdmatcher.group(1);

        // 3、http调用api获取结果
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
            int preIndex = element.data().indexOf(DATA_PRE_KEY);
            int postIndex = element.data().indexOf(DATA_POST_KEY);
            if (preIndex < 0 || postIndex < 0) {
                continue;
            }
            // 解码
            String decodeContent = element.data().substring(preIndex + DATA_PRE_KEY.length(), postIndex);
            DocumentContext context = JsonPath.parse(decodeContent);

            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setVideoId(videoId);
            videoDTO.setVideoSource(VideoSourceEnum.KUAI_SHOU.getCode());
            videoDTO.setVideoOriginalUrl(originalUrl);
            videoDTO.setVideoTitle(new ArrayList<String>(context.read("$..photo.caption")).get(0));
            videoDTO.setVideoCover(new ArrayList<String>(context.read("$..photo.coverUrls[0].url")).get(0));
            videoDTO.setVideoParsedUrl(new ArrayList<String>(context.read("$..photo.mainMvUrls[0].url")).get(0));
            return videoDTO;
        }
        return null;
    }
}