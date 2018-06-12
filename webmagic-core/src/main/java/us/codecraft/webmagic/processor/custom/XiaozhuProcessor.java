package us.codecraft.webmagic.processor.custom;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.custom.utils.ProxyValidation;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * 小猪网西安民宿信息爬取
 *
 * @author Hongyi Zheng
 * @date 2018/6/4
 */
//爬取目标页 http://xa.xiaozhu.com/fangzi/9542691764.html
@TargetUrl("http://xa.xiaozhu.com/fangzi/\\d+\\.html")
//获取目标页的列表页 http://xa.xiaozhu.com/search-duanzufang-p5-0/
@HelpUrl("http://xa.xiaozhu.com/\\w+/")
public class XiaozhuProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(XiaozhuProcessor.class);

    private String URL_TARGET = "http://xa.xiaozhu.com/fangzi/\\d+.html";

    private Site site = Site.me().setRetryTimes(3).setSleepTime((int) (Math.random() * 5000) + 15000);

    @Override
    public void process(Page page) {

        if (page.getUrl().regex(URL_TARGET).match()) {

            //目标页
            logger.info("当前访问:{}", page.getUrl().toString());
            //截取民宿地址
            String address = page.getHtml().xpath("//span[@class='pr5']/text()").toString();
            //截取价格
            String price = page.getHtml().xpath("//*[@id='pricePart']/div/span/text()").toString();
            //截取用户id
            String userId = page.getUrl().regex("\\d+").toString();
            //截取户型
            String type = page.getHtml().xpath("//li[@class='border_none']/p/text()").toString();
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
            //page.addTargetRequests(page.getHtml().links().regex("http://xa\\.xiaozhu\\.com/search-duanzufang-p\\d+-\\d/").all());
            page.addTargetRequests(page.getHtml().xpath("//*[@id='page_list']/ul/li/a[@href]").links().regex("http://xa\\.xiaozhu\\.com/fangzi/\\d+\\.html").all());
            //跳过本页不下载
            page.setSkip(true);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {
        Spider xiaozhu = Spider.create(new XiaozhuProcessor())
                //初始url
                .addUrl("http://xa.xiaozhu.com/search-duanzufang-p13-0/")
//                .addUrl("http://xa.xiaozhu.com/fangzi/23906144103.html")
                //结果保存
                .addPipeline(new JsonFilePipeline("D:\\web-magic\\"))
                //启动10个线程
                .thread(1);

        /*//启动爬虫监控
        SpiderMonitor.instance().register(xiaozhu);
        SpiderMonitor.instance().register(airBnb);
        SpiderMonitor.instance().register(tujia);*/
        /*JSONArray proxyPool = new JSONArray();

        while (null == proxyPool ||proxyPool.size() < 50) {
            GetMethod getProxys = new GetMethod(
                    "http://www.xiongmaodaili.com/xiongmao-web/api/gbip?secret=0d83066cc1f36b0cfbffd06d3b8d53ae&orderNo=GB2018060816164225vhHZvg&count=5&isTxt=0");
            HttpClient httpClient = new HttpClient();
            httpClient.executeMethod(getProxys);
            String result = getProxys.getResponseBodyAsString();
            JSONObject resp = JSONObject.parseObject(result);
            //System.out.println(resp.toJSONString());
            JSONArray proxies = resp.getJSONArray("obj");
            for (int i = 0; i < proxies.size(); i++) {
                if (ProxyValidation.validate(proxies.getJSONObject(i))) {
                    System.out.println("可用==>" + proxies.getJSONObject(i).toJSONString());
                    proxyPool.add(proxies.getJSONObject(i));
                }
            }
            Thread.sleep(11000);
        }


        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy(proxyPool.getJSONObject(0).getString("ip"), Integer.parseInt(proxyPool.getJSONObject(0).getString("port")))

        ));
        xiaozhu.setDownloader(httpClientDownloader);*/
        xiaozhu.start();

    }

}
