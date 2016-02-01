package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.MerchantGoods;

import java.util.List;

/**
 * Created by zhoutc on 2016/2/1.
 */
public interface MerchantGoodsService {

    /**
     * 查询商户业务商品信息
     * @param m_s_g_id 业务绑定商户分类ID
     * @return 商户商品关联信息
     */
    public List<MerchantGoods> queryMerchantGoodsList(int m_s_g_id);

    /**
     * 更新商户业务商品绑定信息
     * @param merchantGoods 商户业务商品绑定信息
     * @return 更新结果
     */
    public boolean updateMerchantGoods(MerchantGoods merchantGoods);

    /**
     * 创建商户业务商品绑定信息
     * @param merchantGoods 商户业务商品绑定信息
     * @return 创建结果
     */
    public boolean createMerchantGoods(MerchantGoods merchantGoods);



}
