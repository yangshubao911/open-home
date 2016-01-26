/**
 * 
 */
package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.api.CategoryManage;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.service.api.CategoryService;
import me.weimi.api.commons.context.RequestContext;

import javax.annotation.Resource;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午4:59:56
 */
public class CategoryManageImpl implements CategoryManage {
	@Resource
	private CategoryService categoryService;

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.dubbo.api.CategoryManage#create(me.weimi.api.commons.context.RequestContext, java.lang.String)
	 */
	@Override
	public String create(RequestContext rc, String json) {
		Category category  = JSON.parseObject(json, Category.class);
		return categoryService.create(category);
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.dubbo.api.CategoryManage#update(me.weimi.api.commons.context.RequestContext, java.lang.String)
	 */
	@Override
	public String update(RequestContext rc, String json) {
		Category category  = JSON.parseObject(json, Category.class);
		return categoryService.update(category);
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.dubbo.api.CategoryManage#list(me.weimi.api.commons.context.RequestContext, int)
	 */
	@Override
	public String list(RequestContext rc, int serviceId) {
		return JSON.toJSONString(this.categoryService.list(serviceId));
	}

}
