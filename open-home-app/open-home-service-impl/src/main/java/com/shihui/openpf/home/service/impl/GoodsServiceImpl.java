/**
 * 
 */
package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.shihui.openpf.home.dao.GoodsDao;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.service.api.GoodsService;
import com.shihui.openpf.home.util.SimpleResponse;
import com.shihui.openpf.home.util.SnapShotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月20日 上午11:15:56
 */
@Service
public class GoodsServiceImpl implements GoodsService {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private GoodsDao goodsDao;

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.GoodsService#create(com.shihui.openpf.home.model.Goods)
	 */
	@Override
	public String create(Goods goods) {
		Goods temp = this.findByCity(goods.getCategoryId(), goods.getCityId());
		if(temp != null){
			return JSON.toJSONString(new SimpleResponse(1,"该城市对应此类商品已存在"));
		}
		
		Date now = new Date();
		//默认商品状态启用
		goods.setGoodsStatus(1);
		goods.setCreateTime(now);
		goods.setUpdateTime(now);
		//默认商品版本为1
		goods.setGoodsVersion(1);
		long id = this.goodsDao.insert(goods);
		//创建商品快照
		goods.setGoodsId(id);
		
		String result = SnapShotUtil.sendSnapShot(goods);
		if(result == null || result.isEmpty()){
			this.goodsDao.delete(goods);
			log.warn("【创建】调用创建商品快照接口失败，删除已创建商品，商品id={}", id);
			return JSON.toJSONString(new SimpleResponse(1,"创建商品快照失败"));
		}
		JSONObject jo = JSONObject.parseObject(result);
		boolean status = jo.getBoolean("status");
        if (!status){
        	this.goodsDao.delete(goods);
			log.warn("【创建】创建商品快照失败，删除已创建商品，商品id={}", id);
			return JSON.toJSONString(new SimpleResponse(1,"创建商品快照失败"));
        }
        int version = jo.getIntValue("version_id");
        goods.setGoodsVersion(version);
        this.goodsDao.update(goods);
		
		return JSON.toJSONString(new SimpleResponse(0, id));
	}

	@Override
	public String update(Goods goods) {
		String result = SnapShotUtil.sendSnapShot(goods);
		if(result == null || result.isEmpty()){
			log.warn("【更新】调用创建商品快照接口失败，商品id={}", goods.getGoodsId());
			return JSON.toJSONString(new SimpleResponse(1,"创建商品快照失败"));
		}
		JSONObject jo = JSONObject.parseObject(result);
		boolean status = jo.getBoolean("status");
        if (!status){
			log.warn("【更新】创建商品快照失败，商品id={}", goods.getGoodsId());
			return JSON.toJSONString(new SimpleResponse(1,"更新商品快照失败"));
        }
        int version = jo.getIntValue("version_id");
        goods.setGoodsVersion(version);
		goods.setUpdateTime(new Date());
		return JSON.toJSONString(new SimpleResponse(0, "更新商品信息成功"));
	}

	@Override
	public Goods findById(long id) {
		String sql = "select * from goods where id=?";
		return this.goodsDao.queryForObject(sql, id);
	}

	@Override
	public Goods findByCity(int categoryId, int cityId) {
		String sql = "select * from goods where category_id=? and city_id=?";
		return this.goodsDao.queryForObject(sql, categoryId, cityId);
	}

	@Override
	public List<Goods> list(int categoryId) {
		String sql = "select * from goods where category_id=?";
		return this.goodsDao.queryForList(sql, categoryId);
	}

}
