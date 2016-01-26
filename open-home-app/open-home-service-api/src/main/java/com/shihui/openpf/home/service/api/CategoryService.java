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

}
