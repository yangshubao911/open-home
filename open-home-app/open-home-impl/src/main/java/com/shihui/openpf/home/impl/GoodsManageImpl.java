/**
 * 
 */
package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.api.GoodsManage;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.service.api.GoodsService;
import me.weimi.api.commons.context.RequestContext;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 上午11:10:59
 */
public class GoodsManageImpl implements GoodsManage {
	@Resource
	private GoodsService goodsService;

	@Override
	public String create(RequestContext rc, String json) {
		Goods goods = JSON.parseObject(json, Goods.class);
		return goodsService.create(goods);
	}

	@Override
	public String update(RequestContext rc, String json) {
		Goods goods = JSON.parseObject(json, Goods.class);
		return goodsService.update(goods);
	}

	@Override
	public String findById(long id) {
		Goods goods = goodsService.findById(id);
		return JSON.toJSONString(goods);
	}

	@Override
	public String findByCity(int categoryId, int cityId) {
		Goods goods = goodsService.findByCity(categoryId, cityId);
		return JSON.toJSONString(goods);
	}

	@Override
	public String list(int categoryId) {
		List<Goods> list = this.goodsService.list(categoryId);
		return JSON.toJSONString(list);
	}

}
