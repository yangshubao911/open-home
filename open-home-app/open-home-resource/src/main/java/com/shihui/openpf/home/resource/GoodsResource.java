/**
 * 
 */
package com.shihui.openpf.home.resource;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.service.api.GoodsService;
import com.shihui.openpf.home.util.SimpleResponse;
import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月19日 下午3:09:50
 */
@Controller
@Path("/v2/openpf/home/goods")
public class GoodsResource {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private GoodsService goodsService;

	@Path("/create")
	@POST
	@BaseInfo(desc = "创建商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String create(@Context RequestContext rc,
	        @ParamDesc(desc = "商品品类id", isRequired = true) @FormParam("category_id") Integer category_id,
	        @ParamDesc(desc = "城市id", isRequired = true) @FormParam("city_id") Integer city_id,
	        @ParamDesc(desc = "城市名称", isRequired = true) @FormParam("city_name") String city_name,
	        @ParamDesc(desc = "商品描述", isRequired = true) @FormParam("goods_desc") String goods_desc,
	        @ParamDesc(desc = "商品名称", isRequired = true) @FormParam("goods_name") String goods_name,
	        @ParamDesc(desc = "商品图片", isRequired = false) @FormParam("image_id") String image_id,
	        @ParamDesc(desc = "商品详情图片", isRequired = true) @FormParam("detail_image") String detail_image,
	        @ParamDesc(desc = "服务类型id", isRequired = true) @FormParam("service_id") Integer service_id,
	        @ParamDesc(desc = "状态", isRequired = true) @FormParam("goods_status") Integer goods_status,
	        @ParamDesc(desc = "实惠抵扣", isRequired = true) @FormParam("sh_off_set") String sh_off_set,
	        @ParamDesc(desc = "首单优惠", isRequired = true) @FormParam("first_sh_off_set") String first_sh_off_set,
	        @ParamDesc(desc = "商品价格", isRequired = true) @FormParam("price") String price,
	        @ParamDesc(desc = "副标题", isRequired = true) @FormParam("goods_subtitle") String goods_subtitle,
	        @ParamDesc(desc = "使用须知", isRequired = true) @FormParam("attention") String attention,
			@ParamDesc(desc = "h5地址", isRequired = false) @FormParam("h5url") String h5url) {
		Goods goods = new Goods();
		goods.setCategoryId(category_id);
		goods.setCityId(city_id);
		goods.setCityName(city_name);
		goods.setGoodsDesc(goods_desc);
		goods.setGoodsName(goods_name);
		goods.setImageId(image_id);
		goods.setDetailImage(detail_image);
		goods.setServiceId(service_id);
		goods.setGoodsStatus(goods_status);
		goods.setShOffSet(sh_off_set);
		goods.setFirstShOffSet(first_sh_off_set);
		goods.setPrice(price);
		goods.setAttention(attention);
		goods.setGoodsSubtitle(goods_subtitle);
		goods.setH5url(h5url);
		String ret = null;
		try {
			ret = goodsService.create(goods);
		} catch (Exception e) {
			log.error("新增商品异常，{}", JSON.toJSONString(goods), e);
			return JSON.toJSONString(new SimpleResponse(1, "创建商品失败"));
		}
		return ret;
	}

	@Path("/update")
	@POST
	@BaseInfo(desc = "更新商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String update(@Context RequestContext rc,
	        @ParamDesc(desc = "商品id", isRequired = true) @FormParam("goods_id") Long goods_id,
	        @ParamDesc(desc = "商品描述", isRequired = false) @FormParam("goods_desc") String goods_desc,
	        @ParamDesc(desc = "商品状态", isRequired = false) @FormParam("goods_status") Integer goods_status,
	        @ParamDesc(desc = "商品名称", isRequired = false) @FormParam("goods_name") String goods_name,
	        @ParamDesc(desc = "商品图片", isRequired = false) @FormParam("image_id") String image_id,
	        @ParamDesc(desc = "商品详情图片", isRequired = false) @FormParam("detail_image") String detail_image,
	        @ParamDesc(desc = "实惠抵扣", isRequired = false) @FormParam("sh_off_set") String sh_off_set,
	        @ParamDesc(desc = "首单优惠", isRequired = false) @FormParam("first_sh_off_set") String first_sh_off_set,
	        @ParamDesc(desc = "商品价格", isRequired = false) @FormParam("price") String price,
	        @ParamDesc(desc = "副标题", isRequired = false) @FormParam("goods_subtitle") String goods_subtitle,
	        @ParamDesc(desc = "使用须知", isRequired = false) @FormParam("attention") String attention,
			 @ParamDesc(desc = "h5地址", isRequired = false) @FormParam("h5url") String h5url) {
		Goods goods = new Goods();
		goods.setGoodsId(goods_id);
		goods.setGoodsDesc(goods_desc);
		goods.setGoodsName(goods_name);
		goods.setImageId(image_id);
		goods.setDetailImage(detail_image);
		goods.setShOffSet(sh_off_set);
		goods.setFirstShOffSet(first_sh_off_set);
		goods.setPrice(price);
		goods.setGoodsStatus(goods_status);
		goods.setAttention(attention);
		goods.setGoodsSubtitle(goods_subtitle);
		goods.setH5url(h5url);
		String ret;
		try {
			ret = goodsService.update(goods);
		} catch (Exception e) {
			log.error("更新商品异常，{}", JSON.toJSONString(goods), e);
			return JSON.toJSONString(new SimpleResponse(1, "更新商品失败"));
		}
		
		return ret;
	}

	@Path("/list")
	@GET
	@BaseInfo(desc = "查询大类下所有商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String list(@Context RequestContext rc,
	        @ParamDesc(desc = "商品分类", isRequired = true) @QueryParam("category_id") int categoryId) {
		return JSON.toJSONString(goodsService.list(categoryId));
	}

	@Path("/listByCity")
	@GET
	@BaseInfo(desc = "查询大类下某个城市商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String listByCity(@Context RequestContext rc,
	        @ParamDesc(desc = "商品分类id", isRequired = true) @QueryParam("category_id") int categoryId,
	        @ParamDesc(desc = "城市id", isRequired = true) @QueryParam("city_id") int cityId) {
		return JSON.toJSONString(goodsService.findByCity(categoryId, cityId));
	}
	
	@Path("/batchUpdate")
	@POST
	@BaseInfo(desc = "批量更新商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String batchUpdate(@Context RequestContext rc,
	        @ParamDesc(desc = "data", isRequired = true) @FormParam("data") String json) {
		List<Goods> goodsList = JSON.parseArray(json, Goods.class);
		return goodsService.batchUpdate(goodsList);
	}

}
