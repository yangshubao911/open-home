package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.dao.MerchantGoodsDao;
import com.shihui.openpf.home.model.MerchantGoods;
import com.shihui.openpf.home.service.api.MerchantGoodsService;
import com.shihui.openpf.home.util.SimpleResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhoutc on 2016/2/1.
 */
@Service
public class MerchantGoodsServiceImpl implements MerchantGoodsService{

    @Resource
    MerchantGoodsDao merchantGoodsDao;

    /**
     * 查询商户业务商品信息
     * @param merchantGoods 商户商品信息
     * @return 商户商品关联信息
     */
    @Override
    public List<MerchantGoods> queryMerchantGoodsList(MerchantGoods merchantGoods){
          return merchantGoodsDao.findByCondition(merchantGoods);
    }

    /**
     * 查询商户业务商品信息
     * @param merchantGoods 商户商品信息
     * @return 商户商品关联信息
     */
    @Override
    public MerchantGoods queryMerchantGoods(MerchantGoods merchantGoods) {
        return merchantGoodsDao.findById(merchantGoods);
    }

    /**
     * 更新商户业务商品绑定信息
     * @param merchantGoods 商户业务商品绑定信息
     * @return 更新结果
     */
    @Override
    public String updateMerchantGoods(MerchantGoods merchantGoods) {
        if(merchantGoodsDao.update(merchantGoods)>0){
            return JSON.toJSONString(new SimpleResponse(0, "更新成功"));
        }else {
            return JSON.toJSONString(new SimpleResponse(1, "更新失败"));
        }
    }

    /**
     * 创建商户业务商品绑定信息
     * @param merchantGoods 商户业务商品绑定信息
     * @return 创建结果
     */
    @Override
    public String createMerchantGoods(MerchantGoods merchantGoods) {
        if(merchantGoodsDao.insert(merchantGoods)>0){
            return JSON.toJSONString(new SimpleResponse(0, "创建成功"));
        }else {
            return JSON.toJSONString(new SimpleResponse(1, "创建失败"));
        }
    }
}
