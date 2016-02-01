package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.home.dao.MerchantGoodsDao;
import com.shihui.openpf.home.model.MerchantGoods;
import com.shihui.openpf.home.service.api.MerchantGoodsService;
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
     * @param m_s_g_id 业务绑定商户分类ID
     * @return 商户商品关联信息
     */
    @Override
    public List<MerchantGoods> queryMerchantGoodsList(int m_s_g_id){
        String sql = "select * from merchant_goods where m_s_g_id = ?";
        return merchantGoodsDao.queryForList(sql,m_s_g_id);
    }

    /**
     * 更新商户业务商品绑定信息
     * @param merchantGoods 商户业务商品绑定信息
     * @return 更新结果
     */
    @Override
    public boolean updateMerchantGoods(MerchantGoods merchantGoods) {
        return merchantGoodsDao.update(merchantGoods)>0;
    }

    /**
     * 创建商户业务商品绑定信息
     * @param merchantGoods 商户业务商品绑定信息
     * @return 创建结果
     */
    @Override
    public boolean createMerchantGoods(MerchantGoods merchantGoods) {
        return merchantGoodsDao.insert(merchantGoods)>0;
    }
}
