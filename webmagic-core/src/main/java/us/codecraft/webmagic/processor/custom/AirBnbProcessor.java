package us.codecraft.webmagic.processor.custom;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.URLEncoder;

/**
 * airbnb 西安民宿信息爬取
 *
 * @author Hongyi Zheng
 * @date 2018/6/12
 */
//爬取目标页 http://xa.xiaozhu.com/fangzi/9542691764.html
@TargetUrl("https://zh.airbnb.com/rooms/\\d+*")
//获取目标页的列表页 http://xa.xiaozhu.com/search-duanzufang-p5-0/
//@HelpUrl("http://xa.xiaozhu.com/\\w+/")
public class AirBnbProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AirBnbProcessor.class);

    private String URL_TARGET = "https://zh.airbnb.com/rooms/\\d+*";

    private Site site = Site.me()
            .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
            .setRetryTimes(3)
            .setSleepTime((int) (Math.random() * 5000));

    @Override
    public void process(Page page) {

        if (page.getUrl().regex(URL_TARGET).match()) {

            //目标页
            logger.info("当前访问:{}", page.getUrl().toString());
            //截取民宿地址
            String address = page.getHtml().xpath("//span[@class='_eamm1ge']/span/text()").toString();
            //截取价格
            String price = page.getHtml().xpath("//span[@class='_doc79r']/span/text()").toString();
            //截取用户id
            String userId = (page.getUrl().regex("/\\d+?location=").toString()).split("\\?")[0];
            //截取户型
            String type = page.getHtml().xpath("//small[@class='_f7heglr']/text()").toString() + " "
                    +page.getHtml().xpath("//div[@class='_2q1l5fu']/text()").toString();
            logger.info("userId = {}, price = {}, address={}, type = {}", userId, price, address,type);

            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(address) && StringUtils.isNotBlank(price)) {
                page.putField("用户Id", userId);
                page.putField("地址", address);
                page.putField("价格", price);
                page.putField("户型", type);
            } else {
                page.setSkip(true);
            }

        } else {
            //列表页
            logger.info("列表页==>{}", page.getUrl().toString());
            //添加后续url
            //page.addTargetRequests(page.getHtml().css("pagination_v2 pb0_vou").links().regex("http://xa\\.xiaozhu\\.com/*").all());
            page.addTargetRequests(page.getHtml().links().regex("https://zh.airbnb.com/rooms/\\d+*").all());
            page.addTargetRequests(page.getHtml().xpath("//ul[@class='_11hau3k']/li/a[@href]").links().regex("https://zh.airbnb.com/w+").all());
            page.addTargetRequests(page.getHtml().xpath("//ul[@class='_11hau3k']/li/a[@href]").links().regex("https://zh.airbnb.com/w+offset=\\d+").all());
            //跳过本页不下载
            page.setSkip(true);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {
        String originalUrl = "https://zh.airbnb.com/s/中国陕西省西安市/homes?refinement_paths%5B%5D=%2Fhomes&guests=1&infants=0&children=0&place_id=ChIJuResIul5YzYRLliUp_1m1IU&fromHome=1&allow_override%5B%5D=&s_tag=OcKcA-H6&adults=1";
        String enc = URLEncoder.encode(originalUrl,"utf-8");
        Spider airbnb = Spider.create(new XiaozhuProcessor())
                //初始url
                .addUrl(enc)
//                .addUrl("http://xa.xiaozhu.com/fangzi/23906144103.html")
                //结果保存
                .addPipeline(new JsonFilePipeline("D:\\web-magic\\"));

        /* //启动爬虫监控
         * SpiderMonitor.instance().register(xiaozhu);
         * SpiderMonitor.instance().register(airBnb);
         *
         * Proxy proxies[] = HttpUtils.getProxyIPs(); // 得到线程池
         * HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
         * httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxies));
         *
         * Spider.setDownloader(httpClientDownloader)
         *
         */
        airbnb.start();

    }

}
