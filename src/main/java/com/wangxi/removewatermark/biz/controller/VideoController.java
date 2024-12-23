/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.biz.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.servicefacade.model.QueryRecordsRequest;
import com.wangxi.removewatermark.common.servicefacade.model.QueryRecordsResult;
import com.wangxi.removewatermark.core.service.resttemplate.RestTemplateService;
import com.wangxi.removewatermark.core.service.video.VideoService;

/**
 * 视频控制器
 *
 * @author wuxi
 * @version $Id: VideoController.java, v 0.1 2024-05-20 16:15 wuxi Exp $$
 * @date 2024/05/21
 */
@RestController
@RequestMapping("removewatermark/video")
public class VideoController {
    /**
     * http请求服务
     */
    @Resource
    private RestTemplateService restTemplateService;

    /**
     * 视频服务
     */
    @Resource
    private VideoService videoService;

    /**
     * 解析视频
     *
     * @param url    url
     * @param userId 用户id
     * @return {@link BaseResult}<{@link Boolean}>
     */
    @RequestMapping(value = "/parseVideo.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult parseVideo(String url, String userId) {
        return videoService.parseVideo(url, userId);
    }

    /**
     * 查询解析记录
     *
     * @param request 请求
     * @return {@link BaseResult}
     */
    @RequestMapping(value = "/queryRecords.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseResult<QueryRecordsResult> queryRecords(@RequestBody QueryRecordsRequest request) {
        return videoService.queryRecords(request);
    }

    /**
     * 转发下载的视频地址
     *
     * @param url url
     * @return {@link ResponseEntity}<{@link String}>
     */
    @RequestMapping(value = "/forwardDownloadUrl.json", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<String> forwardDownloadUrl(String url) {
        // 有些视频地址需要转发才能下载，这里转发一下
        String forwardUrl = restTemplateService.headForHeaders(url).getLocation().toString();
        return ResponseEntity.ok().body(forwardUrl);
    }
}