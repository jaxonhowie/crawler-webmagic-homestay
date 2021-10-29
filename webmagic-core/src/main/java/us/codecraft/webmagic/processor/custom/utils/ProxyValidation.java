package us.codecraft.webmagic.processor.custom.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hongyi Zheng
 * @date 2018/6/8
 */
public class ProxyValidation {

    private static final Logger logger = LoggerFactory.getLogger(ProxyValidation.class);



    public static boolean validate(JSONObject host) throws IOException {
        String ip = host.getString("ip");
        int port = Integer.parseInt(host.getString("port"));
        URL url = null;
        String s = null;
        try {
            url = new URL("http://www.baidu.com/");
        } catch (MalformedURLException e) {
            e.getStackTrace();
        }
        InetSocketAddress addr = null;
        addr = new InetSocketAddress(ip, port);
        // http proxy
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        InputStream in = null;
        try {
            URLConnection conn = url.openConnection(proxy);
            conn.setConnectTimeout(1000);
            in = conn.getInputStream();
            s = convertStreamToString(in);
        } catch (Exception e) {
            //异常IP
            logger.error("ip异常"+addr);
        }finally {
            if (in != null) {
                in.close();
            }
        }

        //有效IP
        return s != null && s.indexOf("baidu") > 0;
    }

    public static String convertStreamToString(InputStream is) {
        if (is == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }
}
