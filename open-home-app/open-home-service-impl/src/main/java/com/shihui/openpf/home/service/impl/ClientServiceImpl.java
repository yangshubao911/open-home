package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.dubbo.api.GroupManage;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.util.StringUtil;
import com.shihui.openpf.home.cache.GoodsCache;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.service.api.ClientService;
import com.shihui.openpf.home.service.api.GoodsService;
import me.weimi.api.commons.json.JSONArray;
import org.springframework.stereotype.Service;
import sun.swing.StringUIClientPropertyKey;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhoutc on 2016/2/20.
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    GroupManage groupManage;

    @Resource
    GoodsService goodsService;

    @Resource
    GoodsCache goodsCache;

    /**
     * 客户端查询商品列表
     *
     * @return 更新结果
     */
    @Override
    public String listGoods(Integer serviceId, Long userId, Long groupId) {
        JSONObject result = new JSONObject();
        Group group = groupManage.getGroupInfoByGid(groupId);
        int cityId = group.getCityId();
        List<Goods> goodsList = goodsService.list(serviceId, cityId);
        JSONArray goods_json = new JSONArray();
        for(Goods good : goodsList){
            JSONObject good_json = new JSONObject();
            good_json.put("serviceId",good.getServiceId());
            good_json.put("goodsId",good.getGoodsId());
            good_json.put("categoryId",good.getCategoryId());
            good_json.put("goodsImage",good.getImageId());
            good_json.put("goodsVersion",good.getGoodsVersion());
            good_json.put("goodsName",good.getGoodsName());
            good_json.put("goodsSubtitle",good.getGoodsSubtitle());
            good_json.put("originalPrice", good.getPrice());
            String pay = StringUtil.decimalSub(good.getPrice(),new String[]{good.getShOffSet()});
            good_json.put("pay",pay);
            good_json.put("shOffset",good.getShOffSet());
            good_json.put("sellNum",goodsCache.querySell(good.getCategoryId()));
            goods_json.add(good_json);
        }
        result.put("list",goods_json);
        return result.toJSONString();
    }
}
