package com.shihui.openpf.home.service.api;


import com.shihui.openpf.common.model.Merchant;

/**
 * Created by zhoutc on 2016/2/1.
 */
public interface MerchantDubboService {

    /**
     * 根据ID查询商户信息
     * @param id 商户ID
     * @return 商户信息
     */
    public Merchant getById(int id);
}
