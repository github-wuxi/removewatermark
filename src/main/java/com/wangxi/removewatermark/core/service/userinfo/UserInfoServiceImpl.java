/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.userinfo;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangxi.removewatermark.common.dal.daointerface.UserInfoDAO;
import com.wangxi.removewatermark.common.dal.dataobject.UserInfoDO;
import com.wangxi.removewatermark.common.servicefacade.enums.ErrorCodeEnum;
import com.wangxi.removewatermark.common.servicefacade.model.BaseResult;
import com.wangxi.removewatermark.common.utils.AssertUtil;
import com.wangxi.removewatermark.core.model.UserLoginResult;
import com.wangxi.removewatermark.core.model.WechatConfig;
import com.wangxi.removewatermark.core.service.resttemplate.RestTemplateService;
import com.wangxi.removewatermark.core.service.template.ServiceCallback;
import com.wangxi.removewatermark.core.service.template.ServiceTemplate;

/**
 * 用户服务实现
 *
 * @author wuxi
 * @version $Id: UserInfoServiceImpl.java, v 0.1 2024-10-02 15:04 wuxi Exp $$
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    /**
     * http请求服务
     */
    @Resource
    private RestTemplateService restTemplateService;

    /**
     * 用户信息dao
     */
    @Resource
    private UserInfoDAO userInfoDAO;

    /**
     * 登录
     *
     * @param code     临时登录凭证 code
     * @param nickName 昵称
     * @return {@link BaseResult}<{@link String}>
     */
    @Override
    public BaseResult<UserLoginResult> login(String code, String nickName) {
        BaseResult<UserLoginResult> baseResult = new BaseResult<>();
        ServiceTemplate.execute(baseResult, new ServiceCallback() {
            @Override
            public void checkParameter() {
                AssertUtil.assertNotBlank(code, "临时登录凭证不能为空");
            }

            @Override
            public void process() {
                UserLoginResult result = new UserLoginResult();
                // 获取用户的小程序标识，即用户id
                String userId = fetchOpenId(code);
                // 用户信息记录
                result.setOpenId(userId);
                result.setTodayFirstTimeLogin(saveUserInfo(userId, nickName));
                baseResult.setResultData(result);
            }

            @Override
            public void finalLog() {
            }

            @Override
            public List<String> extraDigestLogItemList() {
                return null;
            }
        });
        return baseResult;
    }

    /**
     * 获取开放id
     *
     * @param code 代码
     * @return {@link String}
     */
    private String fetchOpenId(String code) {
        // 微信接口说明：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&" +
            "js_code=%s&grant_type=authorization_code", WechatConfig.getAppId(), WechatConfig.getSecret(), code);
        JSONObject result = JSONObject.parseObject(restTemplateService.fetchHttpEntity(url, null,
            HttpMethod.GET, null, String.class));
        AssertUtil.assertNotNull(result, ErrorCodeEnum.USER_INFO_NULL);
        String openid = result.get("openid").toString();
        AssertUtil.assertNotBlank(openid, result.get("errmsg").toString());
        return openid;
    }

    /**
     * 保存用户信息，并且返回是否今日首次登录
     *
     * @param userId   用户id
     * @param userName 用户名
     * @return boolean
     */
    private boolean saveUserInfo(String userId, String userName) {
        QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfoDO::getUserId, userId);
        UserInfoDO userInfoDO = userInfoDAO.selectOne(queryWrapper);
        Date now = new Date();
        // 第一次登录，初始化
        if (userInfoDO == null) {
            userInfoDO = new UserInfoDO();
            userInfoDO.setUserId(userId);
            userInfoDO.setUserName(userName);
            userInfoDO.setWhetherVip(false);
            userInfoDO.setLatestSignInTime(now);
            userInfoDO.setTotalSignInNumber(1L);
            userInfoDO.setAvailableNumber(WechatConfig.getUserSignInAddNumber());
            userInfoDO.setParsedNumber(0L);
            try {
                userInfoDAO.insert(userInfoDO);
            } catch (DuplicateKeyException e) {
                LOGGER.warn(String.format("保存用户信息出现并发重复key（uid：%s），这里弱处理", userId));
            }
            return true;
        }
        userInfoDO.setUserName(userName);
        // 最新签到时间为今天代表不是今天第一次
        if (DateUtils.isSameDay(now, userInfoDO.getLatestSignInTime())) {
            userInfoDO.setLatestSignInTime(now);
            userInfoDAO.updateById(userInfoDO);
            return false;
        }
        userInfoDO.setLatestSignInTime(now);
        userInfoDO.setTotalSignInNumber(userInfoDO.getTotalSignInNumber() + 1L);
        userInfoDO.setAvailableNumber(userInfoDO.getAvailableNumber() + WechatConfig.getUserSignInAddNumber());
        userInfoDAO.updateById(userInfoDO);
        return true;
    }
}