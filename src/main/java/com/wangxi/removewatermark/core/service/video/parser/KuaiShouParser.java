/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * 解析视频
     *
     * @param originalUrl 原始地址
     */
    @Override
    public VideoDTO parseVideo(String originalUrl) {
        // 1、去除掉无用视频后缀描述
        Matcher matcher = Pattern.compile("(https?://v.kuaishou.com/[\\S]*)").matcher(originalUrl);
        if (!matcher.find()) {
            throw new BizException(ErrorCodeEnum.ILLEGAL_VIDEO_URL);
        }
        String url = matcher.group(1);

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
        HttpHeaders httpHeaders = fetchHttpHeaders(url, convertSetCookieToCookie(headForResponse));
        Map<String, Object> params = new HashMap<>();
        params.put("photoId", videoId);
        String content = restTemplateService.fetchHttpEntity("https://m.gifshow.com/rest/wd/photo/info?kpn=KUAISHOU&captchaToken=",
            httpHeaders, HttpMethod.POST, params, String.class);
        if (StringUtils.isBlank(content)) {
            throw new BizException(ErrorCodeEnum.WEB_API_CALL_FAIL);
        }

        // 4、提取出对象
        DocumentContext context = JsonPath.parse(content);

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setVideoId(videoId);
        videoDTO.setVideoSource(VideoSourceEnum.KUAI_SHOU.getCode());
        videoDTO.setVideoOriginalUrl(originalUrl);
        videoDTO.setVideoTitle(context.read("$.photo.caption"));
        videoDTO.setVideoCover(context.read("$.photo.coverUrls[0].url"));
        videoDTO.setVideoParsedUrl(context.read("$.photo.mainMvUrls[0].url"));
        return videoDTO;
    }
}