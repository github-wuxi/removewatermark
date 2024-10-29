/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangxi.removewatermark.common.dal.daointerface.ParseVideoRecordDAO;
import com.wangxi.removewatermark.common.dal.daointerface.UserInfoDAO;
import com.wangxi.removewatermark.common.dal.dataobject.ParseVideoRecordDO;
import com.wangxi.removewatermark.common.dal.dataobject.UserInfoDO;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.servicefacade.enums.VideoSourceEnum;
import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.servicefacade.model.QueryRecordsRequest;
import com.wangxi.removewatermark.common.servicefacade.model.QueryRecordsResult;
import com.wangxi.removewatermark.common.servicefacade.model.VideoDTO;
import com.wangxi.removewatermark.common.utils.AssertUtil;
import com.wangxi.removewatermark.core.service.video.parser.Parser;
import com.wangxi.removewatermark.core.service.template.ServiceCallback;
import com.wangxi.removewatermark.core.service.template.ServiceTemplate;

/**
 * 视频服务实现
 *
 * @author wuxi
 * @version $Id: VideoServiceImpl.java, v 0.1 2024-05-21 14:12 wuxi Exp $$
 * @date 2024/05/21
 */
@Service
public class VideoServiceImpl implements VideoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoServiceImpl.class);

    /**
     * 解析器map
     */
    @Autowired
    private Map<String, Parser> parserMap;

    /**
     * 用户信息dao
     */
    @Resource
    private UserInfoDAO userInfoDAO;

    /**
     * 解析视频记录dao
     */
    @Resource
    private ParseVideoRecordDAO parseVideoRecordDAO;

    /**
     * 解析视频
     *
     * @param url    url
     * @param userId 用户id
     * @return {@link BaseResult}<{@link Boolean}>
     */
    @Override
    public BaseResult parseVideo(String url, String userId) {
        BaseResult baseResult = new BaseResult<>();
        ServiceTemplate.execute(baseResult, new ServiceCallback() {
            @Override
            public void checkParameter() {
                AssertUtil.assertNotBlank(url, "视频url不能为空");
                AssertUtil.assertNotNull(VideoSourceEnum.getByUrl(url), String.format("此链接目前不支持解析，目前仅支持：%s",
                    Arrays.stream(VideoSourceEnum.values()).map(VideoSourceEnum::getDesc).collect(Collectors.toList())));
                AssertUtil.assertNotBlank(userId, "userId不能为空");
            }

            @Override
            public void process() {
                doParseVideo(url, userId);
            }

            @Override
            public void finalLog() {

            }

            @Override
            public List<String> extraDigestLogItemList() {
                List<String> itemList = new ArrayList<>();
                itemList.add(url);
                itemList.add(userId);
                return itemList;
            }
        });
        return baseResult;
    }

    /**
     * 查询记录
     *
     * @param request 请求
     * @return {@link BaseResult}<{@link QueryRecordsResult}>
     */
    @Override
    public BaseResult<QueryRecordsResult> queryRecords(QueryRecordsRequest request) {
        BaseResult<QueryRecordsResult> baseResult = new BaseResult<>();
        ServiceTemplate.execute(baseResult, new ServiceCallback() {
            @Override
            public void checkParameter() {
                AssertUtil.assertNotNull(request, "查询记录请求不能为null");
                AssertUtil.assertNotBlank(request.getUserId(), "userId不能为空");
                AssertUtil.assertMin(request.getPageNum(), 1, "页码不能小于1");
                AssertUtil.assertMin(request.getPageSize(), 1, "页大小不能小于1");
                AssertUtil.assertMax(request.getPageSize(), 100, "页大小不能大于100");
            }

            @Override
            public void process() {
                QueryWrapper<ParseVideoRecordDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("userId", request.getUserId());
                int total = parseVideoRecordDAO.selectCount(queryWrapper);
                IPage<ParseVideoRecordDO> sqlPage = parseVideoRecordDAO.selectPage(
                    new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper.orderByDesc("id"));
                QueryRecordsResult result = new QueryRecordsResult();
                List<VideoDTO> videoList = new ArrayList<>();
                if (total == 0 || sqlPage == null || CollectionUtils.isEmpty(sqlPage.getRecords())) {
                    result.setHasNext(false);
                } else {
                    result.setHasNext(total > request.getPageNum() * request.getPageSize());
                    for (ParseVideoRecordDO recordDO : sqlPage.getRecords()) {
                        VideoDTO videoDTO = new VideoDTO();
                        BeanUtils.copyProperties(recordDO, videoDTO);
                        videoList.add(videoDTO);
                    }
                }
                result.setVideoList(videoList);
                baseResult.setResultData(result);
            }

            @Override
            public void finalLog() {

            }

            @Override
            public List<String> extraDigestLogItemList() {
                List<String> itemList = new ArrayList<>();
                if (request != null) {
                    itemList.add(request.getUserId());
                }
                return itemList;
            }
        });
        return baseResult;
    }

    /**
     * 解析视频
     *
     * @param url    url
     * @param userId 用户id
     */
    private void doParseVideo(String url, String userId) {
        // 1、查询用户数据
        QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfoDO::getUserId, userId);
        UserInfoDO userInfoDO = userInfoDAO.selectOne(queryWrapper);
        AssertUtil.assertNotNull(userInfoDO, ErrorCodeEnum.USER_INFO_NULL);
        AssertUtil.assertTrue(userInfoDO.getAvailableNumber() > 0, ErrorCodeEnum.NONE_AVAILABLE_PARSE_COUNT);

        // 2、视频解析，videoSourceEnum外层保护过了，这里不需要判null
        VideoSourceEnum videoSourceEnum = VideoSourceEnum.getByUrl(url);
        VideoDTO videoDTO = parserMap.get(videoSourceEnum.getParserBeanId()).parseVideo(url);
        AssertUtil.assertNotNull(videoDTO, ErrorCodeEnum.WEB_API_CALL_FAIL);

        // 3、数据事务操作
        userInfoDO.setAvailableNumber(userInfoDO.getAvailableNumber() - 1);
        userInfoDO.setParsedNumber(userInfoDO.getParsedNumber() + 1);

        ParseVideoRecordDO parseVideoRecordDO = new ParseVideoRecordDO();
        parseVideoRecordDO.setUserId(userInfoDO.getUserId());
        BeanUtils.copyProperties(videoDTO, parseVideoRecordDO);

        databaseTransaction(userInfoDO, parseVideoRecordDO);
    }

    /**
     * 数据库事务
     *
     * @param userInfoDO         用户信息
     * @param parseVideoRecordDO 解析视频记录
     */
    @Transactional(timeout = 10)
    public void databaseTransaction(UserInfoDO userInfoDO, ParseVideoRecordDO parseVideoRecordDO) {
        userInfoDAO.updateById(userInfoDO);
        parseVideoRecordDAO.insert(parseVideoRecordDO);
    }
}