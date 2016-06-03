package com.shihui.openpf.home.service.api;

import java.util.List;

import com.shihui.openpf.home.model.MerchantCategory;

/**
 * Created by zhoutc on 2016/2/1.
 */
public interface MerchantCategoryService {

    /**
     * 查询商户业务绑定分类
     * @param merchantCategory 业务绑定商户信息
     * @return 分类信息
     */
    public List<MerchantCategory> queryCategoryList(MerchantCategory merchantCategory);

    /**
     * 更新商户业务分类
     * @param merchantCategory 更新商户业务分类信息
     * @return 更新结果
     */
    public String updateCategory(MerchantCategory merchantCategory);

    /**
     * 创建商户业务分类
     * @param merchantCategory 创建商户业务分类信息
     * @return 创建结果
     */
    public String create(MerchantCategory merchantCategory);
    
    /**
     * 批量绑定分类
     * @param merchantCategorys
     * @return
     */
    public String batchCreate(List<MerchantCategory> merchantCategorys);


    /**
     * 查询商户绑定商品分类
     * @param merchantId
     * @param serviceId
     * @return
     */
    List<MerchantCategory> queryByConditions(int merchantId, int serviceId);

    /**
     * 查询业务商品分类开通的商户
     * @param categoryId
     * @param serviceId
     *
     * @return
     */
    public List<Integer> queryAvailableMerchantId(int categoryId, int serviceId);
}
