package us.codecraft.webmagic.processor.custom;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * airbnb民宿信息爬取
 *
 * @author Hongyi Zheng
 * @date 2018/6/7
 */
//@TargetUrl("")
//@HelpUrl("")
public class AirBnbProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AirBnbProcessor.class);

    private String targetUrl = "";

    private Site site = Site.me().setUserAgent(new HttpHost("127.0.0.1",8888).toString()).setSleepTime((int) (Math.random()*15000));

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(targetUrl).match()) {

        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
