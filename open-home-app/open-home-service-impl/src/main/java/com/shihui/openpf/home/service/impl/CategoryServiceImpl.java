/**
 * 
 */
package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSON;

import com.shihui.openpf.home.dao.CategoryDao;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.service.api.CategoryService;
import com.shihui.openpf.home.util.SimpleResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午4:55:27
 */
@Service
public class CategoryServiceImpl implements CategoryService {
	@Resource
	private CategoryDao categoryDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shihui.openpf.home.dubbo.service.api.CategoryService#create(com.
	 * shihui.openpf.home.dubbo.model.Category)
	 */
	@Override
	public String create(Category category) {
		Date now = new Date();
		category.setStatus(1);
		category.setCreateTime(now);
		category.setUpdateTime(now);
		return JSON.toJSONString(new SimpleResponse(0, categoryDao.insert(category)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shihui.openpf.home.dubbo.service.api.CategoryService#update(com.
	 * shihui.openpf.home.dubbo.model.Category)
	 */
	@Override
	public String update(Category category) {
		Date now = new Date();
		category.setUpdateTime(now);
		if(categoryDao.update(category) > 0){
			return JSON.toJSONString(new SimpleResponse(0, "更新成功"));
		}else{
			return JSON.toJSONString(new SimpleResponse(1, "更新失败"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shihui.openpf.home.dubbo.service.api.CategoryService#list(int)
	 */
	@Override
	public List<Category> list(int serviceId) {
		String sql = "select * from category where service_id=?";
	    List<Category> list = this.categoryDao.queryForList(sql, serviceId);
		return list;
	}

}
