package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.service.api.MerchantDubboService;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/2/1.
 */
public class MerchantDubboServiceImpl implements MerchantDubboService {

    @Resource
    MerchantManage merchantManage;

    @Override
    public Merchant getById(int id) {
        try{
            return merchantManage.getById(id);
        }catch (Exception e){

        }
        return  null;
    }
}
