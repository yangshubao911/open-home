package com.shihui.openpf.home.service.api;

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

    void test();
}
