package com.wangxi.removewatermark;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.wangxi.removewatermark.common.servicefacade.model.VideoDTO;
import com.wangxi.removewatermark.common.utils.AssertUtil;
import com.wangxi.removewatermark.core.service.video.parser.Parser;

/**
 * 视频解析测试
 *
 * @author wuxi
 * @date 2024/06/12
 */
@SpringBootTest
class VideoParserTest {
    /**
     * 抖音解析器
     */
    @Resource
    private Parser douYinParser;

    /**
     * 快手解析器
     */
    @Resource
    private Parser kuaiShouParser;

    @Test
    void testDouYinParseVideo() {
        VideoDTO videoDTO = douYinParser.parseVideo("https://v.douyin.com/i2yCCBtb/");
        System.out.println(videoDTO);
        AssertUtil.assertNotNull(videoDTO, "解析错误！");
    }

    @Test
    void testKuaiShouParseVideo() {
        VideoDTO videoDTO = kuaiShouParser.parseVideo("https://v.kuaishou.com/Fq1fSF 把豆腐和鸡蛋放锅里蒸一蒸，出锅就成了很多饭店里的招牌菜，好吃又下饭 \"上快手学做饭 该作品在快手被播放过219.8万次，点击链接，打开【快手】直接观看！");
        System.out.println(videoDTO);
        AssertUtil.assertNotNull(videoDTO, "解析错误！");
    }
}
