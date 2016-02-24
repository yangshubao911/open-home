package com.shihui.openpf.home.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.dao.MerchantCategoryDao;
import com.shihui.openpf.home.model.MerchantCategory;
import com.shihui.openpf.home.service.api.MerchantCategoryService;
import com.shihui.openpf.home.util.SimpleResponse;

/**
 * Created by zhoutc on 2016/2/1.
 */
@Service
public class MerchantCategoryServiceImpl implements MerchantCategoryService{
	private Logger log = LoggerFactory.getLogger(getClass());

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
     *
     * @param merchantCategory 创建商户业务分类信息
     * @return 创建结果
     */
    @Override
    public String create(MerchantCategory merchantCategory) {
        try {
            boolean result = merchantCategoryDao.save(merchantCategory) > 0;
            if (result)
                return JSON.toJSONString(new SimpleResponse(0, "创建成功"));
        }catch (Exception e){
            log.error("MerchantCategoryService create error!!",e);
        }

        return JSON.toJSONString(new SimpleResponse(1, "创建失败"));

    }

	@Override
	public String batchCreate(List<MerchantCategory> merchantCategorys) { 	 	
		try {
			this.merchantCategoryDao.batchSave(merchantCategorys);
			return JSON.toJSONString(new SimpleResponse(0, "绑定成功"));
		} catch (SQLException e) {
			log.error("批量绑定供应商商品分类异常", e);
			 return JSON.toJSONString(new SimpleResponse(1, "绑定失败"));
		}
	}
}
