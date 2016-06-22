/**
 * 
 */
package com.shihui.openpf.home.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shihui.openpf.home.dao.CategoryDao;
import com.shihui.openpf.home.dao.CategoryRankDao;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.model.CategoryRank;
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
	@Resource
	private CategoryRankDao categoryRankDao;

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

	@Override
	public List<Category> listByCondition(Category category) {
		return categoryDao.findByCondition(category);
	}

	@Override
	public Category findById(Category category) {
		return categoryDao.findById(category);
	}

	@Override
	public List<Category> rankList(int serviceId) {
		String sql = "select * from category where service_id=? and status=1";
	    List<Category> categoryList = this.categoryDao.queryForList(sql, serviceId);
	    CategoryRank cr_query = new CategoryRank();
	    cr_query.setServiceId(serviceId);
	    List<CategoryRank> rankList = categoryRankDao.findByCondition(cr_query);
	    if(rankList.size() == 0){
	    	return categoryList;
	    }
	    final Map<Integer, Integer> rank = new HashMap<>();
	    for(CategoryRank cr : rankList){
	    	rank.put(cr.getCategoryId(), cr.getRank());
	    }
	    
	    for(Category category : categoryList){
	    	category.setRank(rank.get(category.getId()));
	    }
	    
	    Collections.sort(categoryList);
		return categoryList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String rankUpdate(int serviceId, String categoryIds) {
		JSONArray ids = JSON.parseArray(categoryIds);
		//删除旧记录
		CategoryRank cr_query = new CategoryRank();
	    cr_query.setServiceId(serviceId);
	    categoryRankDao.delete(cr_query);
	    
	    for(int i=0; i < ids.size(); i++){
	    	CategoryRank cr = new CategoryRank();
	    	cr.setCategoryId(ids.getInteger(i));
	    	cr.setRank(i+1);
	    	cr.setServiceId(serviceId);
	    	
	    	categoryRankDao.save(cr);
	    }
	    return JSON.toJSONString(new SimpleResponse(0, "更新成功"));
	}
}
