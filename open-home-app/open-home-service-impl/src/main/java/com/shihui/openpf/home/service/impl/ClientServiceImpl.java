package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.dubbo.api.*;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantBusiness;
import com.shihui.openpf.common.service.api.GroupManage;
import com.shihui.openpf.common.util.StringUtil;
import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.cache.GoodsCache;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.model.OrderForm;
import com.shihui.openpf.home.service.api.*;
import com.shihui.openpf.home.util.ChoiceMerhantUtil;
import com.shihui.openpf.home.util.HomeExcepFactor;
import me.weimi.api.app.AppException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhoutc on 2016/2/20.
 */
@Service
public class ClientServiceImpl implements ClientService {
    private Logger log = LoggerFactory.getLogger(getClass());
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
    @Resource
    MerchantAreaManage merchantAreaManage;
    @Resource
    MerchantCategoryService merchantCategoryService;
    @Resource
    MerchantGoodsService merchantGoodsService;
    @Resource
    CurrencyService currencyService;
    @Resource
    HomeServProviderService homeServProviderService;
    @Resource
    OrderSystemService orderSystemService;

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
        if (goods.getGoodsStatus() != 1) {
            throw new AppException(HomeExcepFactor.Goods_Close);
        }
        Category category_search = new Category();
        category_search.setId(goods.getCategoryId());
        Category category = categoryService.findById(category_search);
        if (category == null) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        if (category.getStatus() != 1) {
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
                        JSONObject merchant_json = new JSONObject();
                        merchant_json.put("merchantId", merchant.getMerchantId());
                        merchant_json.put("merchantImage", merchant.getMerchantImage());
                        merchant_json.put("merchantName", merchant.getMerchantName());
                        merchant_json.put("merchantDesc", merchant.getMerchantDesc());
                        merchant_json.put("merchantUrl", merchant.getMerchantLink());
                        merchants.add(merchant_json);
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
        JSONObject result = new JSONObject();
        com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceId);
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }
        Goods goods = goodsService.findById(goodsId);
        if (goods == null) {
            throw new AppException(HomeExcepFactor.Goods_Unfound);
        }
        if (goods.getGoodsStatus() != 1) {
            throw new AppException(HomeExcepFactor.Goods_Close);
        }
        Category search = new Category();
        search.setId(goods.getCategoryId());
        Category category = categoryService.findById(search);
        if (category == null) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        if (category.getStatus() != 1) {
            throw new AppException(HomeExcepFactor.Category_Close);
        }

        Group group = groupManage.getGroupInfoByGid(groupId);
        if (group == null) {
            throw new AppException(HomeExcepFactor.Group_Unfound);
        }
        int cityId = group.getCityId();
        int districtId = group.getDistrictId();
        int plateId = group.getPlateId();

        Merchant merchant_search = new Merchant();
        merchant_search.setMerchantStatus(1);
        List<Merchant> merchantList = merchantManage.getMerchantList(merchant_search);
        Map<Integer, Merchant> merchantMap = new HashMap<>();
        for (Merchant merchant : merchantList) {
            merchantMap.put(merchant.getMerchantId(), merchant);
        }
        List<Integer> m_s_merchantIds = merchantBusinessManage.queryAvailableMerchant(goods.getServiceId());
        Set<Integer> area_merchantIds = merchantAreaManage.getAvailableMerchant(serviceId, cityId, districtId, plateId);
        List<Integer> m_c_merchantIds = merchantCategoryService.queryAvailableMerchantId(goods.getCategoryId(), goods.getServiceId());
        List<Integer> m_g_merchantIds = merchantGoodsService.getAvailableMerchant(goodsId);
        Collection<Integer> collection_1 = CollectionUtils.intersection(m_s_merchantIds, area_merchantIds);
        if (collection_1 == null || collection_1.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Collection<Integer> collection_2 = CollectionUtils.intersection(collection_1, m_c_merchantIds);
        if (collection_2 == null || collection_2.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Collection<Integer> collection_3 = CollectionUtils.intersection(collection_2, m_g_merchantIds);
        if (collection_3 == null || collection_3.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        List<Merchant> available_merchants = new ArrayList<>();
        for (Integer merchantId : collection_3) {
            Merchant merchant = merchantMap.get(merchantId);
            if (merchant != null) {
                available_merchants.add(merchant);
            }
        }
        if (available_merchants == null || available_merchants.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        long balance = currencyService.getUserBalance(userId);
        if (balance == -1) balance = 0;

        JSONObject goods_json = new JSONObject();
        goods_json.put("serviceId", goods.getServiceId());
        goods_json.put("goodsId", goods.getGoodsId());
        goods_json.put("categoryId", goods.getCategoryId());
        goods_json.put("goodsImage", goods.getImageId());
        goods_json.put("goodsVersion", goods.getGoodsVersion());
        goods_json.put("goodsName", goods.getGoodsName());
        goods_json.put("goodsDesc", goods.getGoodsDesc());
        goods_json.put("originalPrice", goods.getPrice());
        goods_json.put("sellNum", goodsCache.querySell(goods.getCategoryId()));

        result.put("goods", goods_json);
        result.put("balance", balance);

        BigDecimal real_offset = null;
        BigDecimal shoffset = null;
        BigDecimal actPrice = null;
        real_offset = costSh == 1 &&
                new BigDecimal(balance).divide(new BigDecimal("100")).compareTo(new BigDecimal(goods.getShOffSet())) >= 0
                ? new BigDecimal(goods.getShOffSet()) : new BigDecimal("0");
        shoffset = new BigDecimal(goods.getShOffSet());

        //showButton 0余额不够不能抵扣  1余额够抵扣  2配置抵扣价格为0
        int showButton = 1;
        if (new BigDecimal(balance).divide(new BigDecimal("100")).compareTo(shoffset) < 0)
            showButton = 0;

        if (shoffset.compareTo(new BigDecimal("0")) == 0) showButton = 2;
        actPrice = new BigDecimal(goods.getPrice()).subtract(real_offset);
        result.put("actPay", actPrice);
        result.put("actOffset", real_offset);
        result.put("showButton", showButton);
        return result.toJSONString();
    }

    /**
     * 客户端查询时间接口
     *
     * @return 返回时间接口
     */
    @Override
    public String queryTime(Integer serviceId, Long userId,
                            Long groupId, Integer categoryId,
                            Integer goodsId, String longitude, String latitude) {
        JSONObject result_json = new JSONObject();
        com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceId);
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }
        Goods goods = goodsService.findById(goodsId);
        if (goods == null) {
            throw new AppException(HomeExcepFactor.Goods_Unfound);
        }
        if (goods.getGoodsStatus() != 1) {
            throw new AppException(HomeExcepFactor.Goods_Close);
        }
        Category search = new Category();
        search.setId(goods.getCategoryId());
        Category category = categoryService.findById(search);
        if (category == null) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        if (category.getStatus() != 1) {
            throw new AppException(HomeExcepFactor.Category_Close);
        }

        Group group = groupManage.getGroupInfoByGid(groupId);
        if (group == null) {
            throw new AppException(HomeExcepFactor.Group_Unfound);
        }
        int cityId = group.getCityId();
        int districtId = group.getDistrictId();
        int plateId = group.getPlateId();

        Merchant merchant_search = new Merchant();
        merchant_search.setMerchantStatus(1);
        List<Merchant> merchantList = merchantManage.getMerchantList(merchant_search);
        Map<Integer, Merchant> merchantMap = new HashMap<>();
        for (Merchant merchant : merchantList) {
            merchantMap.put(merchant.getMerchantId(), merchant);
        }
        List<Integer> m_s_merchantIds = merchantBusinessManage.queryAvailableMerchant(goods.getServiceId());
        Set<Integer> area_merchantIds = merchantAreaManage.getAvailableMerchant(serviceId, cityId, districtId, plateId);
        List<Integer> m_c_merchantIds = merchantCategoryService.queryAvailableMerchantId(goods.getCategoryId(), goods.getServiceId());
        List<Integer> m_g_merchantIds = merchantGoodsService.getAvailableMerchant(goodsId);
        Collection<Integer> collection_1 = CollectionUtils.intersection(m_s_merchantIds, area_merchantIds);
        if (collection_1 == null || collection_1.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Collection<Integer> collection_2 = CollectionUtils.intersection(collection_1, m_c_merchantIds);
        if (collection_2 == null || collection_2.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Collection<Integer> collection_3 = CollectionUtils.intersection(collection_2, m_g_merchantIds);
        if (collection_3 == null || collection_3.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        List<Merchant> available_merchants = new ArrayList<>();
        for (Integer merchantId : collection_3) {
            Merchant merchant = merchantMap.get(merchantId);
            if (merchant != null) {
                available_merchants.add(merchant);
            }
        }
        if (available_merchants == null || available_merchants.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }

        HomeResponse homeResponse = homeServProviderService.getServiceAvailableTime(serviceId, cityId,
                longitude, latitude, available_merchants);

        if (homeResponse.getCode() != 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        String result = homeResponse.getResult();
        JSONArray jsonArray = JSON.parseArray(result);

        if (jsonArray == null) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Set<String> result_days = new HashSet<>();
        Map<String, Map<String, String>> result_times_map = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                JSONObject merchant = jsonArray.getJSONObject(i);
                Integer merchant_id = merchant.getInteger("merchant_id");
                JSONArray jsonArray_times = merchant.getJSONArray("times");
                Set<String> daySet = new HashSet<>();
                for (int j = 0; j < jsonArray_times.size(); j++) {
                    JSONObject time = jsonArray_times.getJSONObject(i);
                    String json_date = time.getString("date");
                    result_days.add(json_date);
                    daySet.add(json_date);
                    String json_timeslot = time.getString("timeslot");
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.set(Calendar.YEAR, Integer.parseInt(json_date.substring(0, 4)));
                    startCalendar.set(Calendar.MONTH, Integer.parseInt(json_date.substring(4, 6)));
                    startCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(json_date.substring(6, 8)));
                    startCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    startCalendar.set(Calendar.MINUTE, 0);
                    startCalendar.set(Calendar.SECOND, 0);
                    startCalendar.set(Calendar.MILLISECOND, 0);

                    for (int k = 0; k < 48; k++) {
                        startCalendar.add(Calendar.MINUTE, 30);
                        if (json_timeslot.charAt(k) == '1') {
                            String time_key = new SimpleDateFormat("yyyyMMddHHmmss").format(startCalendar).substring(8);
                            Map<String, String> times_map = result_times_map.get(json_date);
                            if (times_map == null) {
                                times_map = new HashMap<>();
                                times_map.put(time_key, String.valueOf(merchant_id));
                                result_times_map.put(json_date, times_map);
                            } else {
                                String value = times_map.get(time_key) + "," + String.valueOf(merchant_id);
                                times_map.put(time_key, value);
                            }
                        }
                    }

                }

            } catch (Exception e) {
                log.error("goodsId:{} parse time error!!", goodsId, e);
            }
        }

        JSONObject times_json = new JSONObject();
        for (Map.Entry entry : result_times_map.entrySet()) {
            String key = (String) entry.getKey();
            Map<String, String> value = (Map<String, String>) entry.getKey();

            JSONArray times_array = new JSONArray();
            for (Map.Entry entry1 : value.entrySet()) {
                JSONObject json = new JSONObject();
                String times = (String) entry1.getKey();
                String merchants = (String) entry1.getValue();
                String client_time = times.substring(0, 2) + " " + times.substring(2, 4);
                json.put(client_time, merchants.split(","));
                times_array.add(json);
            }

            times_json.put(key, times_array);
        }

        result_json.put("status", 0);
        result_json.put("msg", "查询时间成功");
        result_json.put("days", result_days.toArray());
        result_json.put("times", times_json);
        return result_json.toJSONString();
    }

    /**
     * 客户端创建订单接口
     *
     * @return 返回时间接口
     */
    @Override
    public String orderCreate(OrderForm orderForm) {
        JSONObject result_json = new JSONObject();
        com.shihui.openpf.common.model.Service service = serviceManage.findById(orderForm.getServiceId());
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }
        Goods goods = goodsService.findById(orderForm.getGoodsId());
        if (goods == null) {
            throw new AppException(HomeExcepFactor.Goods_Unfound);
        }
        if (goods.getGoodsStatus() != 1) {
            throw new AppException(HomeExcepFactor.Goods_Close);
        }
        Category search = new Category();
        search.setId(goods.getCategoryId());
        Category category = categoryService.findById(search);
        if (category == null) {
            throw new AppException(HomeExcepFactor.Category_Unfound);
        }
        if (category.getStatus() != 1) {
            throw new AppException(HomeExcepFactor.Category_Close);
        }

        Group group = groupManage.getGroupInfoByGid(orderForm.getGroupId());
        if (group == null) {
            throw new AppException(HomeExcepFactor.Group_Unfound);
        }
        int cityId = group.getCityId();
        int districtId = group.getDistrictId();
        int plateId = group.getPlateId();

        Merchant merchant_search = new Merchant();
        merchant_search.setMerchantStatus(1);
        List<Merchant> merchantList = merchantManage.getMerchantList(merchant_search);
        Map<Integer, Merchant> merchantMap = new HashMap<>();
        for (Merchant merchant : merchantList) {
            merchantMap.put(merchant.getMerchantId(), merchant);
        }
        List<Integer> m_s_merchantIds = new ArrayList<>();
        Map<Integer, Integer> mbMap = new HashMap();
        MerchantBusiness merchantBusiness = new MerchantBusiness();
        merchantBusiness.setServiceId(orderForm.getServiceId());
        merchantBusiness.setStatus(1);
        List<MerchantBusiness> m_s_merchantIdList = merchantBusinessManage.queryList(merchantBusiness);
        for (MerchantBusiness mb : m_s_merchantIdList) {
            m_s_merchantIds.add(mb.getMerchantId());
            mbMap.put(mb.getMerchantId(), mb.getWeight());
        }


        Set<Integer> area_merchantIds = merchantAreaManage.getAvailableMerchant(orderForm.getServiceId(), cityId, districtId, plateId);
        List<Integer> m_c_merchantIds = merchantCategoryService.queryAvailableMerchantId(goods.getCategoryId(), goods.getServiceId());
        List<Integer> m_g_merchantIds = merchantGoodsService.getAvailableMerchant(orderForm.getGoodsId());
        Collection<Integer> collection_1 = CollectionUtils.intersection(m_s_merchantIds, area_merchantIds);
        if (collection_1 == null || collection_1.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Collection<Integer> collection_2 = CollectionUtils.intersection(collection_1, m_c_merchantIds);
        if (collection_2 == null || collection_2.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }
        Collection<Integer> collection_3 = CollectionUtils.intersection(collection_2, m_g_merchantIds);
        if (collection_3 == null || collection_3.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }

        String[] merchants = orderForm.getMerchants().split(",");
        Map<Integer, String> map = new HashMap();
        for (String merchantId : merchants) {
            map.put(Integer.parseInt(merchantId), "");
        }

        Map<Integer, Integer> choiceMap = new HashMap<>();
        List<Merchant> available_merchants = new ArrayList<>();
        for (Integer merchantId : collection_3) {
            Merchant merchant = merchantMap.get(merchantId);
            if (merchant != null) {
                if (map.get(merchantId) != null) {
                    available_merchants.add(merchant);
                    choiceMap.put(merchantId, mbMap.get(merchantId));
                }
            }
        }
        if (available_merchants == null || available_merchants.size() == 0) {
            throw new AppException(HomeExcepFactor.Merchant_Unfound);
        }

        long time = System.currentTimeMillis();
        HomeResponse homeResponse = null;
        while (choiceMap.size() > 0) {
            int choice_merchantId = ChoiceMerhantUtil.choiceMerchant(choiceMap);

                    homeResponse = homeServProviderService.isServiceAvailable(merchantMap.get(choice_merchantId),
                    orderForm.getServiceId(), orderForm.getGoodsId(),
                    orderForm.getGroupId(), orderForm.getLongitude(),
                    orderForm.getLatitude(), orderForm.getServiceTime());

            if (homeResponse.getCode() != 0) {
                choiceMap.remove(choice_merchantId);
            }else{
                break;
            }

            if(System.currentTimeMillis()-time>3*1000){
                break;
            }
        }
        if (homeResponse == null || homeResponse.getCode() != 0) {
            throw new AppException(HomeExcepFactor.Merchant_Refresh);
        }
        long balance = currencyService.getUserBalance(orderForm.getUserId());
        if (balance == -1) balance = 0;
        BigDecimal real_offset = null;
        BigDecimal shoffset = null;
        BigDecimal actPrice = null;
        real_offset = orderForm.getCostSh() == 1 &&
                new BigDecimal(balance).divide(new BigDecimal("100")).compareTo(new BigDecimal(goods.getShOffSet())) >= 0
                ? new BigDecimal(goods.getShOffSet()) : new BigDecimal("0");
        shoffset = new BigDecimal(goods.getShOffSet());
        actPrice = new BigDecimal(goods.getPrice()).subtract(real_offset);
        if (actPrice.compareTo(new BigDecimal(orderForm.getActPay())) != 0 ||
                real_offset.compareTo(new BigDecimal(orderForm.getActOffset())) != 0) {
            throw new AppException(HomeExcepFactor.Price_Wrong);
        }
        String json = "";
        String result = orderSystemService.submitOrder(json);



        return null;
    }
}
