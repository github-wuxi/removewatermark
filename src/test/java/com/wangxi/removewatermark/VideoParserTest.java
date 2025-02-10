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

    /**
     * 小红书解析器
     */
    @Resource Parser redNoteParser;

    @Test
    void testDouYinParseVideo() {
        VideoDTO videoDTO = douYinParser.parseVideo("2.89 复制打开抖音，看看【舌尖的作品】羊肉的奶香味与韭花酱的咸香碰撞融合，这一口内蒙古手... https://v.douyin.com/CeiJLLMMS/ sRK:/ 05/27 H@V.yt");
        System.out.println(videoDTO);
        AssertUtil.assertNotNull(videoDTO, "解析错误！");
    }

    @Test
    void testKuaiShouParseVideo() {
        VideoDTO videoDTO = kuaiShouParser.parseVideo("https://v.kuaishou.com/Fq1fSF 把豆腐和鸡蛋放锅里蒸一蒸，出锅就成了很多饭店里的招牌菜，好吃又下饭 \"上快手学做饭 该作品在快手被播放过219.8万次，点击链接，打开【快手】直接观看！");
        System.out.println(videoDTO);
        AssertUtil.assertNotNull(videoDTO, "解析错误！");
    }

    @Test
    void testRedNoteParseVideo() {
        VideoDTO videoDTO = redNoteParser.parseVideo("99 糖浆发布了一篇小红书笔记，快来看吧！ \uD83D\uDE06 zL8udRRVXk5ngXh \uD83D\uDE06 http://xhslink.com/a/BQzf08nDAly5，复制本条信息，打开【小红书】App查看精彩内容！\n");
        System.out.println(videoDTO);
        AssertUtil.assertNotNull(videoDTO, "解析错误！");
    }
}
