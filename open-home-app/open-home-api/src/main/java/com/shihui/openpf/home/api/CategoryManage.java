/**
 * 
 */
package com.shihui.openpf.home.api;

import me.weimi.api.commons.context.RequestContext;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午4:57:39
 */
public interface CategoryManage {

	/**
	 * 创建商品分类
	 * 
	 * @param rc
	 * @param json
	 *            javabean 序列化json，字段值：name,desc,img_url,service_id
	 * @return
	 */
	public String create(RequestContext rc, String json);

	/**
	 * 更新商品分类
	 * 
	 * @param rc
	 * @param json
	 *            javabean 序列化json，字段值：id,name,status,desc,img_url,service_id
	 * @return
	 */
	public String update(RequestContext rc, String json);

	/**
	 * 查询商品分类
	 * 
	 * @param rc
	 * @param serviceId
	 * @return javabean
	 *         序列化json，字段值：id,name,status,desc,img_url,service_id,create_time,
	 *         update_time
	 */
	public String list(RequestContext rc, int serviceId);

}
