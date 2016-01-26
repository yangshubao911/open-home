/**
 * 
 */
package com.shihui.openpf.home.api;

import me.weimi.api.commons.context.RequestContext;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 上午11:05:09
 */
public interface GoodsManage {

	/**
	 * 创建商品
	 * 
	 * @param rc
	 * @param json
	 *            javabean 对象序列化json 字段： goods_name,img_url,city_id,price
	 *            ,sh_off_set,goods_desc, category_id,service_id
	 * @return
	 */
	public String create(RequestContext rc, String json);

	/**
	 * 更新商品
	 * 
	 * @param rc
	 * @param json
	 *            javabean 对象序列化json 字段： goods_id,goods_name,img_url,city_id
	 *            ,price,sh_off_set,goods_desc,goods_status, category_id,
	 *            service_id
	 * @return
	 */
	public String update(RequestContext rc, String json);

	/**
	 * 根据id查询商品
	 * 
	 * @param id
	 * @return 返回商品对象序列化json
	 */
	public String findById(long id);

	/**
	 * 查询商品
	 * 
	 * @param categoryId
	 *            商品分类id
	 * @param cityId
	 *            城市id
	 * @return 返回商品对象序列化json
	 */
	public String findByCity(int categoryId, int cityId);

	/**
	 * 查询商品
	 * 
	 * @param categoryId
	 *            商品分类id
	 * @return 返回商品对象序列化json
	 */
	public String list(int categoryId);

}
