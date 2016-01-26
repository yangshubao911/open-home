package com.shihui.openpf.home.api;

import me.weimi.api.commons.context.RequestContext;

/**
 * Created by zhoutc on 2016/1/21.
 */
public interface OrderManage {

    /**
     * 客户端创建订单
     * @param json 创建订单请求数据
     *
     * @return 返回结果
     */
    public String createOrder(RequestContext rc, String json);

    /**
     * 客户端创建订单
     * @param json 创建订单请求数据
     *
     * @return 返回结果
     */
    public String queryOrder(RequestContext rc, String json, int page, int size);

}
