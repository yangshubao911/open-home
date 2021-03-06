package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.OrderForm;
import me.weimi.api.commons.context.RequestContext;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by zhoutc on 2016/2/20.
 */
public interface ClientService {

    /**
     * 客户端查询商品列表
     *
     * @return 返回商品列表
     */
    public String listGoods(Integer serviceId, Long userId , Long groupId, Long mid , RequestContext rc, HttpServletRequest request , int cityId);

    /**
     * 客户端查询商品详情
     *
     * @return 返回商品接口
     */
    public String detail(Integer serviceId, Long userId, Long groupId , Integer categoryId , Integer goodsId , Long mid , RequestContext rc, HttpServletRequest request, int cityId);

    /**
     * H5分享页面商品详情接口
     *
     * @return 返回商品详情接口
     */
    public String detail(Integer goodsId);


    /**
     * 客户端订单确认接口
     *
     * @return 返回订单详情
     */
    public String orderConfirm(RequestContext rc, Integer serviceId, Long userId, Long groupId , Integer categoryId , Integer goodsId, Integer costSh);

      /**
     * 客户端查询时间接口
     *
     * @return 返回时间接口
     */
    public String queryTime(Integer serviceId, Long userId, Long groupId , Integer categoryId , Integer goodsId, String longitude, String latitude);

    /**
     * 客户端创建订单接口
     *
     * @return 返回时间接口
     */
    public String orderCreate(OrderForm orderForm, RequestContext rc);


    /**
     * 测试修改订单状态
     *
     * @return 修改结果
     */
    public boolean testOrder(long orderId , int status);

}
