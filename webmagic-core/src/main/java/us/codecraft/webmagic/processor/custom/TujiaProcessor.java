package us.codecraft.webmagic.processor.custom;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author Hongyi Zheng
 * @date 2018/6/7
 */
public class TujiaProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(XiaozhuProcessor.class);

    private String targetUrl = "";

    private Site site = Site.me().setSleepTime((int) (Math.random()*15000) +5000);

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(targetUrl).match()) {
            logger.info("当前访问：{}",page.getUrl().toString());
            //address
            String addr = page.getHtml().xpath("").toString();
            String price = page.getHtml().xpath("").toString();
            String userId = page.getUrl().regex("").toString();
            //户型
            String type = page.getHtml().xpath("").toString();
            logger.info("userId = {}, price = {}, address={}, type = {}", userId, price, addr,type);

            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(addr) && StringUtils.isNotBlank(price)) {
                //保存需要的参数
                page.putField("用户Id", userId);
                page.putField("地址", addr);
                page.putField("价格", price);
                page.putField("户型", type);
            }else {
                //设置此页跳过
                page.setSkip(true);
            }
        }else {
            //列表页
            logger.info("列表页==>{}", page.getUrl().toString());
            //添加后续url
            //page.addTargetRequests(page.getHtml().css("pagination_v2 pb0_vou").links().regex("http://xa\\.xiaozhu\\.com/*").all());
            //page.addTargetRequests(page.getHtml().links().regex("http://xa\\.xiaozhu\\.com/search-duanzufang-p\\d+-\\d/").all());
            page.addTargetRequests(page.getHtml().xpath("//*[@id='page_list']/ul/li/a[@href]").links().regex("http://xa\\.xiaozhu\\.com/fangzi/\\d+\\.html").all());
            //列表页跳过
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {
        Spider tujia = Spider.create(new TujiaProcessor())
                .addUrl("")
                .addPipeline(new JsonFilePipeline("D:\\web-magic\\"))
                .thread(1);
        tujia.start();
    }
}
