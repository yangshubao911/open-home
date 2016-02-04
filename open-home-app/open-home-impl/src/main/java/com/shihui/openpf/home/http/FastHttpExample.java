package com.shihui.openpf.home.http;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SongJian on 2016/1/22.
 */
public class FastHttpExample {

    public static void main(String[] args) {
        long s1 = System.currentTimeMillis();
        CloseableHttpAsyncClient client = FastHttpUtils.defaultHttpAsyncClient();
        List<HttpCallbackHandler<String>> rs = new ArrayList<>();
        /**
         * example FastHttpGet code 1
         */
        for (int i = 0; i < 200; i++) {
            HttpGet get = new FastHttpGet("http://php.weather.sina.com.cn/iframe/index/w_cl.php")
                    .addStringParameters("code", "js", "day", "0", "city", "北京", "dfc", "1", "charset", "utf-8")
                    .build();
            rs.add(FastHttpUtils.executeReturnStringHandler(client, get));
        }
       
        for (int i = 0; i < rs.size(); i++) {
            System.out.println(rs.get(i).get());
        }
        System.out.println("takes -- " + (System.currentTimeMillis() - s1));
        /**
         * example code FastHttpPost 2
         */
        try {
            s1 = System.currentTimeMillis();
            System.out.println(
                    FastHttpUtils.executeGetReturnString(client, "http://php.weather.sina.com.cn/iframe/index/w_cl.php",
                            "code", "js", "day", "0", "city", "北京", "dfc", "1", "charset", "utf-8"));
            System.out.println("takes2 -- " + (System.currentTimeMillis() - s1));
        } catch (UnsupportedEncodingException e) {
        }

        /**
         * example code FastHttpPost 3
         */
        try {
            HttpPost post = new FastHttpPost("http://php.weather.sina.com.cn/iframe/index/w_cl.php")
                    .addStringParameters("code", "js", "day", "0", "city", "北京", "dfc", "1", "charset", "utf-8").build();
            System.out.println(FastHttpUtils.executeReturnStringHandler(client, post).get());
        } catch (UnsupportedEncodingException e) {
        }

        /**
         * example FastHttpPost code 4
         */
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "js");
        map.put("day", "0");
        map.put("city", "上海");
        map.put("dfc", "1");
        map.put("charset", "utf-8");

        try {
            HttpPost post2 = new FastHttpPost("http://php.weather.sina.com.cn/iframe/index/w_cl.php").build();
            System.out.println(FastHttpUtils.executeReturnStringHandler(client, post2, map).get());
        } catch (UnsupportedEncodingException e) {
        }
        System.out.println("takes -- " + (System.currentTimeMillis() - s1));
        FastHttpUtils.close(client);
    }

}
