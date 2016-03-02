package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.OrderForm;
import me.weimi.api.commons.context.RequestContext;

import java.awt.*;

/**
 * Created by zhoutc on 2016/2/20.
 */
public interface ClientService {

    /**
     * 客户端查询商品列表
     *
     * @return 返回商品列表
     */
    public String listGoods(Integer serviceId, Long userId , Long groupId);

    /**
     * 客户端查询商品详情
     *
     * @return 返回商品接口
     */
    public String detail(Integer serviceId, Long userId, Long groupId , Integer categoryId , Integer goodsId);

    /**
     * 客户端订单确认接口
     *
     * @return 返回订单详情
     */
    public String orderConfirm(Integer serviceId, Long userId, Long groupId , Integer categoryId , Integer goodsId, Integer costSh);

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
    public String orderCreate(OrderForm orderForm,RequestContext rc);


}
