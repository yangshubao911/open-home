package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.dubbo.api.GroupManage;
import com.shihui.openpf.common.dubbo.api.MerchantBusinessManage;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.dubbo.api.ServiceManage;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantBusiness;
import com.shihui.openpf.common.util.StringUtil;
import com.shihui.openpf.home.cache.GoodsCache;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.service.api.CategoryService;
import com.shihui.openpf.home.service.api.ClientService;
import com.shihui.openpf.home.service.api.GoodsService;
import com.shihui.openpf.home.util.HomeExcepFactor;
import me.weimi.api.app.AppException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    ServiceManage serviceManage;
    @Resource
    CategoryService categoryService;

    /**
     * 客户端查询商品列表
     *
     * @return 返回商品列表
     */
    @Override
    public String listGoods(Integer serviceId, Long userId, Long groupId) {
        JSONObject result = new JSONObject();

        com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceId);
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }
        Group group = groupManage.getGroupInfoByGid(groupId);
        if (group == null) {
            throw new AppException(HomeExcepFactor.Group_Unfound);
        }
        int cityId = group.getCityId();
        List<Goods> goodsList = goodsService.list(serviceId, cityId);
        if (goodsList == null || goodsList.size() == 0) {
            throw new AppException(HomeExcepFactor.Goods_Unfound);
        }
        Category category_search = new Category();
        category_search.setServiceId(serviceId);
        category_search.setStatus(1);
        List<Category> categories = categoryService.listByCondition(category_search);
        if (categories == null || categories.size() == 0) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        Map<Integer, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }
        JSONArray goods_jsonArray = new JSONArray();
        for (Goods goods : goodsList) {
            JSONObject goods_json = new JSONObject();
            if (goods.getGoodsStatus() == 1 && categoryMap.get(goods.getCategoryId()) != null) {
                goods_json.put("serviceId", goods.getServiceId());
                goods_json.put("goodsId", goods.getGoodsId());
                goods_json.put("categoryId", goods.getCategoryId());
                goods_json.put("goodsImage", goods.getImageId());
                goods_json.put("goodsVersion", goods.getGoodsVersion());
                goods_json.put("goodsName", goods.getGoodsName());
                goods_json.put("goodsSubtitle", goods.getGoodsSubtitle());
                //活动计算价格
                //
                //活动计算价格
                goods_json.put("originalPrice", goods.getPrice());
                String pay = StringUtil.decimalSub(goods.getPrice(), new String[]{goods.getShOffSet()});
                goods_json.put("pay", pay);
                goods_json.put("shOffset", goods.getShOffSet());


                goods_json.put("sellNum", goodsCache.querySell(goods.getCategoryId()));
                goods_jsonArray.add(goods_json);
            }

        }
        result.put("list", goods_jsonArray);
        return result.toJSONString();
    }

    /**
     * 客户端查询商品详情
     *
     * @return 返回商品接口
     */
    @Override
    public String detail(Integer serviceId, Long userId, Long groupId, Integer categoryId, Integer goodsId) {
        JSONObject result = new JSONObject();

        com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceId);
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }

        Goods goods = goodsService.findById(goodsId);
        if (goods == null) {
            throw new AppException(HomeExcepFactor.Goods_Unfound);
        }
        if(goods.getGoodsStatus()!=1){
            throw new AppException(HomeExcepFactor.Goods_Close);
        }
        Category category_search = new Category();
        category_search.setId(goods.getCategoryId());
        Category category = categoryService.findById(category_search);
        if (category == null) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        if(category.getStatus()!=1) {
            throw new AppException(HomeExcepFactor.Category_Close);
        }

        JSONObject goods_json = new JSONObject();
        goods_json.put("serviceId", goods.getServiceId());
        goods_json.put("goodsId", goods.getGoodsId());
        goods_json.put("categoryId", goods.getCategoryId());
        goods_json.put("detailImage", goods.getDetailImage());
        goods_json.put("goodsVersion", goods.getGoodsVersion());
        goods_json.put("goodsName", goods.getGoodsName());
        goods_json.put("goodsSubtitle", goods.getGoodsSubtitle());
        goods_json.put("goodsdesc", goods.getGoodsDesc());
        goods_json.put("attention", goods.getAttention());
        //活动计算价格
        //
        //活动计算价格
        goods_json.put("originalPrice", goods.getPrice());
        String pay = StringUtil.decimalSub(goods.getPrice(), new String[]{goods.getShOffSet()});
        goods_json.put("pay", pay);
        goods_json.put("shOffset", goods.getShOffSet());
        goods_json.put("sellNum", goodsCache.querySell(goods.getCategoryId()));
        result.put("goods", goods_json);

        MerchantBusiness search = new MerchantBusiness();
        search.setServiceId(serviceId);
        search.setStatus(1);
        List<MerchantBusiness> merchantBusinesses = merchantBusinessManage.queryList(search);
        List<Integer> searchlist = new ArrayList<>();
        if (merchantBusinesses != null && merchantBusinesses.size() > 0) {
            for (MerchantBusiness merchantBusiness : merchantBusinesses) {
                searchlist.add(merchantBusiness.getMerchantId());
            }
            if (searchlist != null && searchlist.size() > 0) {
                List<Merchant> merchantList = merchantManage.batchQuery(searchlist);
                JSONArray merchants = new JSONArray();
                if (merchantList != null && merchantList.size() > 0) {
                    for (Merchant merchant : merchantList) {
                        if (merchant.getMerchantStatus() == 1) {
                            JSONObject merchant_json = new JSONObject();
                            merchant_json.put("merchantId", merchant.getMerchantId());
                            merchant_json.put("merchantImage", merchant.getMerchantImage());
                            merchant_json.put("merchantName", merchant.getMerchantName());
                            merchant_json.put("merchantDesc", merchant.getMerchantDesc());
                            merchant_json.put("merchantUrl", merchant.getMerchantLink());
                            merchants.add(merchant_json);
                        }
                    }
                    result.put("merchants", merchants);
                }

            }
        }
        return result.toJSONString();
    }

    /**
     * 客户端订单确认接口
     *
     * @return 返回订单详情
     */
    @Override
    public String orderConfirm(Integer serviceId, Long userId, Long groupId, Integer categoryId, Integer goodsId, Integer costSh) {

        com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceId);
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }
        Goods goods = goodsService.findById(goodsId);
        if (goods == null) {
            throw new AppException(HomeExcepFactor.Goods_Unfound);
        }
        if(goods.getGoodsStatus()!=1){
            throw new AppException(HomeExcepFactor.Goods_Close);
        }
        Category search = new Category();
        search.setId(goods.getCategoryId());
        Category category = categoryService.findById(search);
        if (category == null) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        if(category.getStatus()!=1) {
            throw new AppException(HomeExcepFactor.Category_Close);
        }







        return null;
    }
}
