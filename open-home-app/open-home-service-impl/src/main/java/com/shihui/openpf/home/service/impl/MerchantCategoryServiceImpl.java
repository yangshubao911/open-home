package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.dao.MerchantCategoryDao;
import com.shihui.openpf.home.model.MerchantCategory;
import com.shihui.openpf.home.service.api.MerchantCategoryService;
import com.shihui.openpf.home.util.SimpleResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhoutc on 2016/2/1.
 */
@Service
public class MerchantCategoryServiceImpl implements MerchantCategoryService{

    @Resource
    MerchantCategoryDao merchantCategoryDao;

    /**
     * 查询商户业务绑定分类
     * @param merchantCategory 业务绑定商户信息
     * @return 分类信息
     */
    @Override
    public List<MerchantCategory> queryCategoryList(MerchantCategory merchantCategory) {
        return merchantCategoryDao.findByCondition(merchantCategory);
    }

    /**
     * 更新商户业务分类
     * @param merchantCategory 更新商户业务分类信息
     * @return 更新结果
     */
    @Override
    public String updateCategory(MerchantCategory merchantCategory) {
       if(merchantCategoryDao.update(merchantCategory)>0){
           return JSON.toJSONString(new SimpleResponse(0, "更新成功"));
       }else {
           return JSON.toJSONString(new SimpleResponse(1, "更新失败"));
       }
    }

    /**
     * 创建商户业务分类
     * @param merchantCategory 创建商户业务分类信息
     * @return 创建结果
     */
    @Override
    public String create(MerchantCategory merchantCategory) {
        if(merchantCategoryDao.insert(merchantCategory)>0){
            return JSON.toJSONString(new SimpleResponse(0, "创建成功"));
        }else {
            return JSON.toJSONString(new SimpleResponse(1, "创建失败"));
        }
    }
}
