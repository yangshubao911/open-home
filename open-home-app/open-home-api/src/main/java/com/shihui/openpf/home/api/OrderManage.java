package com.shihui.openpf.home.api;

import com.shihui.openpf.home.model.OrderCancelType;
import me.weimi.api.commons.context.RequestContext;
import com.shihui.openpf.home.model.Order;


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
     * @param order 创建订单请求数据
     *
     * @return 返回结果
     */
    public String queryOrderList(RequestContext rc, Order order, String startTime, String endTime ,int page, int size);

    /**
     * 查询订单详情
     * @param orderId 订单ID
     *
     * @return 返回订单详情
     */
    public String queryOrder(long orderId);

    /**
     * 查询第三方订单详情
     * @param orderId 订单ID
     *
     * @return 返回订单详情
     */
    public String queryThirdOrder(String key,Integer serviceType,String orderId,String version,String sign);

    /**
     * 取消第三方订单
     *
     * @param orderId 订单ID
     * @return 返回取消订单结果
     */
    public String cancelThirdOrder(String key,int serviceType,String orderId,String version,String sign);

     /**
     * 更新第三方订单
     *
     * @param orderId 订单ID
     * @return 返回更新订单结果
     */
    public String updateThirdOrder(String key,int serviceType,String orderId,String version,String sign, int status);


    /**
     * 取消平台订单
     *
     * @param orderId 订单ID
     * @return 返回取消订单结果
     */
    public String cancelLocalOrder(long orderId , OrderCancelType orderCancelType);


    /**
     * 查询异常订单
     *
     * @return 返回订单详情
     */
    public String countunusual();

    /**
     * 查询异常订单
     * @return 订单列表
     */
    public String queryUnusual();

    /**
     * 导出异常订单
     * @return 订单列表
     */
    public String exportUnusual();
}
