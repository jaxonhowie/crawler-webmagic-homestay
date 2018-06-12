package us.codecraft.webmagic.processor.DynamicProxy;

/**
 * @author Hongyi Zheng
 * @date 2018/6/7
 */
public class DynamicProxy {

    /**
     *
     * 拼装请求头
     *
     * @param orderno 订单号
     * @param secret 密码
     * @param timestamp 时间戳
     * @param change false-换ip ，true-不换ip
     * @return
     */
    public static String authHeader(String orderno, String secret, int timestamp,String change){
        //拼装签名字符串
        String planText = String.format("orderno=%s,secret=%s,timestamp=%d", orderno, secret, timestamp);

        //计算签名
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(planText).toUpperCase();

        //拼装请求头Proxy-Authorization的值;change 参数: false-换ip ,true-不换ip
        return String.format("sign=%s&orderno=%s&timestamp=%d&change=%s", sign, orderno, timestamp,change);
    }





}
