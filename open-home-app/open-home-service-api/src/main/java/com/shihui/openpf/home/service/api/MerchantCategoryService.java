package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.MerchantCategory;

import java.util.List;

/**
 * Created by zhoutc on 2016/2/1.
 */
public interface MerchantCategoryService {

    /**
     * 查询商户业务绑定分类
     * @param m_s_c_id 业务绑定商户ID
     * @return 分类信息
     */
    public List<MerchantCategory> queryCategoryList(int m_s_c_id);

    /**
     * 更新商户业务分类
     * @param merchantCategory 更新商户业务分类信息
     * @return 更新结果
     */
    public String updateCategory(MerchantCategory merchantCategory);

}
