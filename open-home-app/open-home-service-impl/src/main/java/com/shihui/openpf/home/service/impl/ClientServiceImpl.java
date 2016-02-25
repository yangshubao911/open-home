package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.dubbo.api.GroupManage;
import com.shihui.openpf.common.dubbo.api.MerchantBusinessManage;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantBusiness;
import com.shihui.openpf.common.util.StringUtil;
import com.shihui.openpf.home.cache.GoodsCache;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.service.api.ClientService;
import com.shihui.openpf.home.service.api.GoodsService;
import com.shihui.openpf.home.util.HomeExcepFactor;
import me.weimi.api.app.AppException;
import me.weimi.api.commons.json.JSONArray;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    MerchantManage merchantManage;
    @Resource
    MerchantBusinessManage merchantBusinessManage;

    /**
     * 客户端查询商品列表
     *
     * @return 返回商品列表
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
            //活动计算价格
            //
            //活动计算价格
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

    /**
     * 客户端查询商品详情
     *
     * @return 返回商品接口
     */
    @Override
    public String detail(Integer serviceId, Long userId, Long groupId , Integer categoryId , Integer goodsId) {

        Group group = groupManage.getGroupInfoByGid(groupId);
        int cityId = group.getCityId();

        MerchantBusiness search = new MerchantBusiness();
        search.setServiceId(serviceId);
        search.setStatus(1);
        List<MerchantBusiness> merchantBusinesses =  merchantBusinessManage.queryList(search);
        List<Integer> searchlist = new ArrayList<>();
        if(merchantBusinesses==null||merchantBusinesses.size()==0) return null;
        for(MerchantBusiness merchantBusiness : merchantBusinesses){
            searchlist.add(merchantBusiness.getMerchantId());
        }
        List<Merchant> merchantList =  merchantManage.batchQuery(searchlist);
        if(merchantList==null||merchantList.size()==0) return null;
        for(Merchant merchant : merchantList){

        }

        return null;
    }

    @Override
    public void test() {
        throw new AppException(HomeExcepFactor.TEST);
    }
}
