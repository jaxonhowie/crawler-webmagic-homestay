package us.codecraft.webmagic.processor.custom;
// 新用户替换29行的secret和orderno即可运行本示例代码
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;


public class TestDynamic {
    //change 参数: false-换ip ，true-不换ip
    public static String authHeader(String orderno, String secret, int timestamp,String change){
        //拼装签名字符串
        String planText = String.format("orderno=%s,secret=%s,timestamp=%d", orderno, secret, timestamp);

        //计算签名
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(planText).toUpperCase();

        //拼装请求头Proxy-Authorization的值;change 参数: false-换ip ,true-不换ip
        return String.format("sign=%s&orderno=%s&timestamp=%d&change=%s", sign, orderno, timestamp,change);
    }

    public static void main(String[] args) throws IOException {

    }
}