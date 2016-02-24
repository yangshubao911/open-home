package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.MerchantGoods;

import java.util.List;

/**
 * Created by zhoutc on 2016/2/1.
 */
public interface MerchantGoodsService {

	/**
	 * 查询商户业务商品信息
	 * 
	 * @param merchantGoods
	 *            商户商品信息
	 * @return 商户商品关联信息
	 */
	public List<MerchantGoods> queryMerchantGoodsList(MerchantGoods merchantGoods);

	/**
	 * 查询商户业务商品信息
	 * 
	 * @param merchantGoods
	 *            商户商品信息
	 * @return 商户商品关联信息
	 */
	public MerchantGoods queryMerchantGoods(MerchantGoods merchantGoods);

	/**
	 * 更新商户业务商品绑定信息
	 * 
	 * @param merchantGoods
	 *            商户业务商品绑定信息
	 * @return 更新结果
	 */
	public String updateMerchantGoods(MerchantGoods merchantGoods);

	/**
	 * 创建商户业务商品绑定信息
	 * 
	 * @param merchantGoods
	 *            商户业务商品绑定信息
	 * @return 创建结果
	 */
	public String createMerchantGoods(MerchantGoods merchantGoods);

	/**
	 * 批量绑定商品
	 * @param list
	 * @return
	 */
	public String batchAddGoods(List<MerchantGoods> list);
	
	/**
	 * 批量更新绑定商品
	 * @param list
	 * @return
	 */
	public String batchUpdateAddedGoods(List<MerchantGoods> list);
	
	/**
	 * 查询商户已绑定商品
	 * @param merchantId
	 * @param serviceId
	 * @param categoryId
	 * @return
	 */
	List<MerchantGoods> findByConditions(Integer merchantId, Integer serviceId, Integer categoryId);

}
