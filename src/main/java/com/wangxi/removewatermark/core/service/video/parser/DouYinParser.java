/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video.parser;

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
     * 数据key
     */
    private static final String DATA_KEY = "window._ROUTER_DATA = ";

    /**
     * 数据路径
     */
    private static final String DATA_PATH = "$.loaderData.['video_(id)/page'].videoInfoRes.item_list[0].";

    /**
     * 解析视频
     *
     * @param originalUrl 原始地址
     */
    @Override
    public VideoDTO parseVideo(String originalUrl) {
        // 1、去除掉无用视频前后缀
        String url = fetchTargetUrl(originalUrl, "(https?://v.douyin.com/[\\S]*)");

        // 2、获取重定向后的地址，来获取视频id
        url = restTemplateService.headForHeaders(url).getLocation().toString();
        Matcher matcher = Pattern.compile("/share/video/([\\d]*)[/|?]").matcher(url);
        if (!matcher.find()) {
            throw new BizException(ErrorCodeEnum.ILLEGAL_VIDEO_URL);
        }
        String videoId = matcher.group(1);
        if (StringUtils.isBlank(videoId)) {
            throw new BizException(ErrorCodeEnum.ILLEGAL_VIDEO_URL);
        }

        // 3、http调用api获取结果
        String dyWebApi = "https://www.iesdouyin.com/share/video/" + videoId;
        HttpHeaders httpHeaders = fetchHttpHeaders(dyWebApi, null);
        String content = restTemplateService.fetchHttpEntity(dyWebApi, httpHeaders, HttpMethod.GET, null, String.class);
        if (StringUtils.isBlank(content)) {
            throw new BizException(ErrorCodeEnum.WEB_API_CALL_FAIL);
        }

        // 4、解析出html标签，找到DATA_KEY对应的元素
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
            // 5、提取出对象
            DocumentContext context = JsonPath.parse(decodeContent);
            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setVideoId(videoId);
            videoDTO.setVideoSource(VideoSourceEnum.DOU_YIN.getCode());
            videoDTO.setVideoOriginalUrl(originalUrl);
            videoDTO.setVideoTitle(context.read(DATA_PATH + "desc"));
            videoDTO.setVideoCover(context.read(DATA_PATH + "video.cover.url_list[0]"));
            // 6、抖音这里aweme.snssdk.com的视频地址无法小程序直接下载，需要获取重定向后的地址
            String needLocationUrl = ((String) context.read(DATA_PATH + "video.play_addr.url_list[0]"))
                .replace("playwm", "play");
            videoDTO.setVideoParsedUrl(restTemplateService.headForHeaders(needLocationUrl).getLocation().toString());
            return videoDTO;
        }
        return null;
    }
}