package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.Order;


/**
 * Created by zhoutc on 2016/1/21.
 */
public interface OrderManage {

    /**
     * 客户端创建订单
     * @param order 创建订单请求数据
     *
     * @return 返回结果
     */
    public String queryOrderList(Order order, String startTime, String endTime ,int page, int size);

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
     *  平台取消订单
     * @param userId
     * @param email
     * @param orderId
     * @param price
     * @param reason
     * @param refundSHCoin 是否退实惠现金，1-是，2-否
     * @return 返回取消订单结果
     */
    public String cancelLocalOrder(long userId, String email, long orderId , String price, String reason, int refundSHCoin);


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

    /**
     * 导出异常订单
     * @return 订单列表
     */
    public String yjzConverter(String orderId,int status,String pId, String version,String methodName, String sign);
}
