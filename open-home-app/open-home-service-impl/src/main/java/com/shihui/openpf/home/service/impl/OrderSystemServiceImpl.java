package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.home.http.FastHttpPost;
import com.shihui.openpf.home.http.FastHttpUtils;
import com.shihui.openpf.home.http.HttpCallbackHandler;
import com.shihui.openpf.home.model.OrderForm;
import com.shihui.openpf.home.service.api.OrderSystemService;
import com.shihui.openpf.home.util.OpenHomeConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoutc on 2016/2/29.
 */
@Service
public class OrderSystemServiceImpl implements OrderSystemService {

    @Override
    public String submitOrder(String json) {

        CloseableHttpAsyncClient client = FastHttpUtils.defaultHttpAsyncClient();
        List<HttpCallbackHandler<String>> rs = new ArrayList<>();

        try{
            HttpPost post = new FastHttpPost(OpenHomeConfig.order_submit_url)
                    .addStringParameters("order", json).build();
           return FastHttpUtils.executeReturnStringHandler(client, post).get();
        }catch (Exception e){

        }
        return null;
    }
}
