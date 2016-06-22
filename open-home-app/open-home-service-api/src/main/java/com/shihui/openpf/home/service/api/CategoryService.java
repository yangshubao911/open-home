/**
 *
 */
package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.Category;

import java.util.List;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午4:45:19
 */
public interface CategoryService {

	public String create(Category category);

	public String update(Category category);

	public List<Category> list(int serviceId);

	public List<Category> listByCondition(Category category);

	public Category findById(Category category);
			
    /**
     * 查询商品类型，按照商品类型排序输出
     * @param serviceId
     * @return
     */
    public List<Category> rankList(int serviceId);
    
    /**
     * 保存商品类型排序
     * @param serviceId
     * @param categoryIds 
     *    商品类型id数组，按id顺序保存商品类型排序
     * @return
     */
    public String rankUpdate(int serviceId, String categoryIds);
}
