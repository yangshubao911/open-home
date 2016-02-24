/**
 * 
 */
package com.shihui.openpf.home.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.dao.CategoryDao;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.service.api.CategoryService;
import com.shihui.openpf.home.util.SimpleResponse;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午4:55:27
 */
@Service
public class CategoryServiceImpl implements CategoryService {
	Logger log = LoggerFactory.getLogger(getClass());
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
		category.setCreateTime(now);
		category.setUpdateTime(now);
		long id = 0;
		try {
			id = categoryDao.insert(category);
		} catch (Exception e) {
			log.error("创建商品类型异常，{}", JSON.toJSONString(category), e);
			return JSON.toJSONString(new SimpleResponse(1, "创建商品类型失败"));
		}
		return JSON.toJSONString(new SimpleResponse(0, id));
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
		int result = 0;
		try {
			result = categoryDao.update(category);
		} catch (Exception e) {
			log.error("更新商品类型异常，{}", JSON.toJSONString(category), e);
		}
		
		if(result > 0){
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
