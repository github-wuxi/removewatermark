/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2024 All Rights Reserved.
 */
package com.wangxi.removewatermark.core.service.userinfo;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import com.wangxi.removewatermark.common.utils.exception.BizException;
import com.wangxi.removewatermark.common.servicefacade.model.UserLoginResult;
import com.wangxi.removewatermark.core.model.FileConfig;
import com.wangxi.removewatermark.core.model.WechatConfig;
import com.wangxi.removewatermark.core.service.file.FileService;
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
     * 文件服务
     */
    @Resource
    private FileService fileService;

    /**
     * 登录
     *
     * @param code      临时登录凭证 code
     * @return {@link BaseResult}<{@link UserLoginResult}>
     */
    @Override
    public BaseResult<UserLoginResult> login(String code) {
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
                if (saveUserInfo(userId)) {
                    result.setTodayFirstTimeLoginText(String.format("每日首次登录可获取%s次免费解析次数～",
                        WechatConfig.getUserSignInAddNumber()));
                }
                baseResult.setResultData(result);
            }

            @Override
            public void finalLog() {
                LOGGER.debug(String.format("用户登录，入参[code:%s], 出参[baseResult:%s]",
                    code, baseResult));
            }

            @Override
            public List<String> extraDigestLogItemList() {
                return null;
            }
        });
        return baseResult;
    }

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return {@link BaseResult}<{@link UserInfoDO}>
     */
    @Override
    public BaseResult<UserInfoDO> queryUserInfo(String userId) {
        BaseResult<UserInfoDO> baseResult = new BaseResult<>();
        ServiceTemplate.execute(baseResult, new ServiceCallback() {
            @Override
            public void checkParameter() {
                AssertUtil.assertNotBlank(userId, "用户id不能为空");
            }

            @Override
            public void process() {
                QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(UserInfoDO::getUserId, userId);
                UserInfoDO userInfoDO = userInfoDAO.selectOne(queryWrapper);
                baseResult.setResultData(userInfoDO);
            }

            @Override
            public void finalLog() {
                LOGGER.debug(String.format("查询用户信息，入参[userId:%s], 出参[baseResult:%s]", userId, baseResult));
            }

            @Override
            public List<String> extraDigestLogItemList() {
                return Collections.singletonList(userId);
            }
        });
        return baseResult;
    }

    /**
     * 更新用户信息
     *
     * @param userId     用户id
     * @param userName   用户名
     * @return {@link BaseResult}
     */
    @Override
    public BaseResult updateUserInfo(String userId, String userName) {
        BaseResult baseResult = new BaseResult<>();
        ServiceTemplate.execute(baseResult, new ServiceCallback() {
            @Override
            public void checkParameter() {
                AssertUtil.assertNotBlank(userId, "用户id不能为空");
                AssertUtil.assertNotBlank(userName, "用户名称不能为空");
            }

            @Override
            public void process() {
                QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(UserInfoDO::getUserId, userId);
                UserInfoDO userInfoDO = userInfoDAO.selectOne(queryWrapper);
                AssertUtil.assertNotNull(userInfoDO, "用户信息查询为空！");
                userInfoDO.setUserName(userName);
                userInfoDAO.updateById(userInfoDO);
            }

            @Override
            public void finalLog() {
                LOGGER.debug(String.format("更新用户信息，入参[userId:%s, userName:%s], 出参[baseResult:%s]",
                    userId, userName, baseResult));
            }

            @Override
            public List<String> extraDigestLogItemList() {
                return Collections.singletonList(userId);
            }
        });
        return baseResult;
    }

    /**
     * 上传用户头像
     *
     * @param userId          用户id
     * @param avatarUrlBase64 头像url base64
     * @return {@link BaseResult}<{@link String}>
     */
    @Override
    public BaseResult<String> uploadUserAvatar(String userId, String avatarUrlBase64) {
        BaseResult<String> baseResult = new BaseResult<>();
        ServiceTemplate.execute(baseResult, new ServiceCallback() {
            @Override
            public void checkParameter() {
                AssertUtil.assertNotBlank(userId, "用户id不能为空");
                AssertUtil.assertNotBlank(avatarUrlBase64, "用户头像不能为空");
            }

            @Override
            public void process() {
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] urlBytes = decoder.decode(avatarUrlBase64);
                String newFileName = userId + new Date().getTime() + ".png";
                // 新用户头像存到服务器
                fileService.sftpUpload(urlBytes, newFileName, FileConfig.getAvatarFilePath());

                QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(UserInfoDO::getUserId, userId);
                UserInfoDO userInfoDO = userInfoDAO.selectOne(queryWrapper);
                AssertUtil.assertNotNull(userInfoDO, "用户信息查询为空！");

                // 新头像存成功后，旧头像删除
                if (StringUtils.isNotBlank(userInfoDO.getUserAvatar())) {
                    // 正则表达式，匹配以/为分隔符后的最后一部分
                    String regex = "([^/]+\\.png)$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(userInfoDO.getUserAvatar());
                    if (matcher.find()) {
                        fileService.delete(FileConfig.getAvatarFilePath(), matcher.group(1));
                    }
                }
                // 对应nginx配置文件访问，更新新头像
                String userAvatar = FileConfig.getAvatarFilePrefix() + newFileName;
                userInfoDO.setUserAvatar(userAvatar);
                userInfoDAO.updateById(userInfoDO);

                baseResult.setResultData(userAvatar);
            }

            @Override
            public void finalLog() {
                LOGGER.debug(String.format("上传用户头像，入参[userId:%s, avatarUrlBase64:%s], 出参[baseResult:%s]",
                    userId, avatarUrlBase64, baseResult));
            }

            @Override
            public List<String> extraDigestLogItemList() {
                return Collections.singletonList(userId);
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
        if (result == null || result.get("openid") == null) {
            throw new BizException(ErrorCodeEnum.USER_INFO_NULL);
        }
        String openid = result.get("openid").toString();
        if (StringUtils.isBlank(openid)) {
            String errMsg = result.get("errmsg") == null ? "用户微信开放id获取为空" : result.get("errmsg").toString();
            throw new BizException(ErrorCodeEnum.ILLEGAL_ARGUMENTS, errMsg);
        }
        return openid;
    }

    /**
     * 保存用户信息，并且返回是否今日首次登录
     *
     * @param userId    用户id
     * @return boolean
     */
    private boolean saveUserInfo(String userId) {
        QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfoDO::getUserId, userId);
        UserInfoDO userInfoDO = userInfoDAO.selectOne(queryWrapper);
        Date now = new Date();
        // 第一次登录，初始化
        if (userInfoDO == null) {
            userInfoDO = new UserInfoDO();
            userInfoDO.setUserId(userId);
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
        userInfoDO.setGmtModified(now);
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