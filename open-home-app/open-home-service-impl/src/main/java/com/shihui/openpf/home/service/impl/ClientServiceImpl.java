package com.shihui.openpf.home.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import com.shihui.openpf.common.tools.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.common.enums.OrderTypeEnum;
import com.shihui.api.order.vo.ApiResult;
import com.shihui.api.order.vo.SingleGoodsCreateOrderParam;
import com.shihui.openpf.common.dubbo.api.MerchantAreaManage;
import com.shihui.openpf.common.dubbo.api.MerchantBusinessManage;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.dubbo.api.ServiceManage;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantBusiness;
import com.shihui.openpf.common.service.api.GroupManage;
import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.cache.GoodsCache;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.model.Contact;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.model.MerchantGoods;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderForm;
import com.shihui.openpf.home.model.OrderInfo;
import com.shihui.openpf.home.model.Request;
import com.shihui.openpf.home.service.api.CategoryService;
import com.shihui.openpf.home.service.api.ClientService;
import com.shihui.openpf.home.service.api.ContactService;
import com.shihui.openpf.home.service.api.CurrencyService;
import com.shihui.openpf.home.service.api.GoodsService;
import com.shihui.openpf.home.service.api.MerchantCategoryService;
import com.shihui.openpf.home.service.api.MerchantGoodsService;
import com.shihui.openpf.home.service.api.OrderService;
import com.shihui.openpf.home.service.api.OrderSystemService;
import com.shihui.openpf.home.service.api.RequestService;
import com.shihui.openpf.home.util.ChoiceMerhantUtil;
import com.shihui.openpf.home.util.HomeExcepFactor;

import me.weimi.api.app.AppException;
import me.weimi.api.commons.context.RequestContext;

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
    @Resource
    ContactService contactService;
    @Resource
    RequestService requestService;
    @Resource
    OrderService orderService;

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

        //showButton 0不使用实惠现金或者实惠现金不够抵用  1使用实惠现金并且实惠现金够抵用
        int showButton = 1;
        if(balance==0) showButton=0;
        if(shoffset.compareTo(new BigDecimal("0"))==0)showButton=0;
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
        Set<String> result_days = new TreeSet<>();
        Map<String, Map<String, String>> result_times_map = new TreeMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            try {
                JSONObject results = jsonArray.getJSONObject(i);
                JSONObject json_rs = results.getJSONObject("result");
                Integer merchant_id = results.getInteger("merchant_id");
                JSONArray jsonArray_times = json_rs.getJSONArray("times");
                Set<String> daySet = new HashSet<>();
                for (int j = 0; j < jsonArray_times.size(); j++) {
                    JSONObject time = jsonArray_times.getJSONObject(j);
                    String json_date = time.getString("date");
                    json_date =  json_date.substring(0, 4) + "-" + json_date.substring(4, 6) + "-" + json_date.substring(6, 8);
                    result_days.add(json_date);
                    daySet.add(json_date);
                    String json_timeslot = time.getString("timeslot");
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.set(Calendar.YEAR, Integer.parseInt(json_date.substring(0, 4)));
                    startCalendar.set(Calendar.MONTH, Integer.parseInt(json_date.substring(5, 7)));
                    startCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(json_date.substring(8, 10)));
                    startCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    startCalendar.set(Calendar.MINUTE, 0);
                    startCalendar.set(Calendar.SECOND, 0);
                    startCalendar.set(Calendar.MILLISECOND, 0);

                    for (int k = 0; k < 48; k++) {
                        startCalendar.add(Calendar.MINUTE, 30);
                        if (json_timeslot.charAt(k) == '1') {
                            String time_key = new SimpleDateFormat("yyyyMMddHHmmss").format(startCalendar.getTime()).substring(8);
                            Map<String, String> times_map = result_times_map.get(json_date);
                            if (times_map == null) {
                                times_map = new TreeMap<>();
                                times_map.put(time_key, String.valueOf(merchant_id));
                                result_times_map.put(json_date, times_map);
                            } else {
                                String time_value = times_map.get(time_key);
                                String value = "";
                                if (time_value == null)
                                    value = String.valueOf(merchant_id);
                                else
                                    value = times_map.get(time_key) + "," + String.valueOf(merchant_id);
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
        Map times_map = new TreeMap<>();
        for (Map.Entry entry : result_times_map.entrySet()) {
            String key = (String) entry.getKey();
            Map<String, String> value = (Map<String, String>) entry.getValue();
            JSONArray times_array = new JSONArray();
            for (Map.Entry entry1 : value.entrySet()) {
                JSONObject json = new JSONObject();
                String times = (String) entry1.getKey();
                String merchants = (String) entry1.getValue();
                String client_time = times.substring(0, 2) + ":" + times.substring(2, 4);
                json.put(client_time, merchants.split(","));
                times_array.add(json);
            }

            times_map.put(key, times_array);
        }

        result_json.put("status", 0);
        result_json.put("msg", "查询时间成功");
        result_json.put("days", result_days.toArray());
        result_json.put("times", times_map);
        return result_json.toJSONString();
    }

    /**
     * 客户端创建订单接口
     *
     * @return 返回时间接口
     */
    @Override
    public String orderCreate(OrderForm orderForm, RequestContext rc) {
        JSONObject result_json = new JSONObject();
        com.shihui.openpf.common.model.Service service = serviceManage.findById(orderForm.getServiceId());
        if (service.getServiceStatus() != 1) {
            throw new AppException(HomeExcepFactor.Service_Close);
        }
        Goods goods_search = new Goods();
        goods_search.setGoodsId(orderForm.getGoodsId());
        goods_search.setGoodsVersion(orderForm.getGoodsVersion());
        Goods goods = goodsService.findById(goods_search);
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
        int proviceId = group.getProvinceId();

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

        MerchantGoods merchantGoods_search = new MerchantGoods();
        merchantGoods_search.setGoodsId(orderForm.getGoodsId());
        List<MerchantGoods> merchantGoodsList = merchantGoodsService.queryMerchantGoodsList(merchantGoods_search);
        List<Integer> m_g_merchantIds = new ArrayList<>();
        Map<Integer, String> mgMap = new HashMap();
        for (MerchantGoods merchantGoods : merchantGoodsList) {
            m_g_merchantIds.add(merchantGoods.getMerchantId());
            mgMap.put(merchantGoods.getMerchantId(), merchantGoods.getSettlement());
        }

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
        Integer merchantId = null;
        while (choiceMap.size() > 0) {
            int choice_merchantId = ChoiceMerhantUtil.choiceMerchant(choiceMap);

            homeResponse = homeServProviderService.isServiceAvailable(merchantMap.get(choice_merchantId),
                    orderForm.getServiceId(), orderForm.getCategoryId(),
                    orderForm.getGroupId(), orderForm.getLongitude(),
                    orderForm.getLatitude(), orderForm.getServiceTime());

            if (homeResponse.getCode() != 0) {
                choiceMap.remove(choice_merchantId);
            } else {
                merchantId = choice_merchantId;
                break;
            }

            if (System.currentTimeMillis() - time > 3 * 1000) {
                break;
            }
        }
        if (merchantId == null || homeResponse == null || homeResponse.getCode() != 0) {
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
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAmount(1);
        orderInfo.setCategoryExtend(category.getExtend());
        orderInfo.setCityId(cityId);
        orderInfo.setContactName(orderForm.getContactName());
        orderInfo.setDetailAddress(orderForm.getDetailAddress());
        orderInfo.setExtend(null);
        orderInfo.setGoodsId(orderForm.getGoodsId());
        orderInfo.setLatitude(orderForm.getLatitude());
        orderInfo.setLongitude(orderForm.getLongitude());
        orderInfo.setPhone(orderForm.getServicePhone());
        orderInfo.setPrice(mgMap.get(merchantId));
        orderInfo.setRemark(orderForm.getRemark());
        orderInfo.setServiceAddress(orderForm.getServiceAddress());
        orderInfo.setServiceStartTime(orderForm.getServiceTime());

        HomeResponse create_third_part = homeServProviderService.createOrder(merchantMap.get(merchantId), goods.getServiceId(), orderInfo);
        if (create_third_part.getCode() != 0) {
            throw new AppException(HomeExcepFactor.Third_Order_Fail);
        }
        JSONObject third_order = JSONObject.parseObject(create_third_part.getResult());
        JSONObject third_order_result = third_order.getJSONObject("result");
        String requestId = third_order_result.getString("orderId");

        Date now = new Date();
        SingleGoodsCreateOrderParam singleGoodsCreateOrderParam = new SingleGoodsCreateOrderParam();
        singleGoodsCreateOrderParam.setCampaignId(1);
        singleGoodsCreateOrderParam.setCityId(cityId);
        singleGoodsCreateOrderParam.setCommunityId((int) orderForm.getGroupId());
        singleGoodsCreateOrderParam.setExt("");
        singleGoodsCreateOrderParam.setOriginPrice(StringUtil.yuan2hao(goods.getPrice()));
        singleGoodsCreateOrderParam.setIp(rc.getIp());
        singleGoodsCreateOrderParam.setGoodsVersion(goods.getGoodsVersion());
        singleGoodsCreateOrderParam.setGoodsId(goods.getGoodsId());
        singleGoodsCreateOrderParam.setGoodsName(goods.getGoodsName());
        singleGoodsCreateOrderParam.setUserId(orderForm.getUserId());
        singleGoodsCreateOrderParam.setOrderType(OrderTypeEnum.DoorTDoor.getValue());

        long overTime = System.currentTimeMillis() + 1000 * 30;
        singleGoodsCreateOrderParam.setOverdueTime(overTime);
        singleGoodsCreateOrderParam.setMerchantId(merchantMap.get(merchantId).getMerchantCode());
        singleGoodsCreateOrderParam.setPrice(StringUtil.yuan2hao(orderForm.getActPay()));
        singleGoodsCreateOrderParam.setOffset(StringUtil.yuan2hao(orderForm.getActOffset()));
        singleGoodsCreateOrderParam.setProvinceId(proviceId);
        singleGoodsCreateOrderParam.setDistrictId(districtId);
        singleGoodsCreateOrderParam.setContactName(orderForm.getContactName());
        singleGoodsCreateOrderParam.setContactTel(orderForm.getServicePhone());
        singleGoodsCreateOrderParam.setContactAddress(orderForm.getServiceAddress() + orderForm.getDetailAddress());
        long startTime = 0;
        try {
            Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(orderForm.getServiceTime() + "00");
            startTime = date.getTime();

        } catch (Exception e) {
            log.error("{} parse time error!", orderForm.getServiceTime(), e);
        }
        singleGoodsCreateOrderParam.setPlanStartTime(startTime);
        singleGoodsCreateOrderParam.setPlanEndTime(0);
        singleGoodsCreateOrderParam.setPlanEndTime(0);
        if (StringUtil.isEmpty(orderForm.getRemark()))
            singleGoodsCreateOrderParam.setUserRemark("");
        else
            singleGoodsCreateOrderParam.setUserRemark(orderForm.getRemark());
        ApiResult result = orderSystemService.submitOrder(singleGoodsCreateOrderParam);

        if (result.getStatus() != 1) {
            return JSON.toJSONString(result);
        }

        long orderId = Long.parseLong(result.getOrderId().get(0));

        Contact contact = new Contact();
        contact.setOrderId(orderId);
        contact.setContactName(orderForm.getContactName());
        contact.setDetailAddress(orderForm.getDetailAddress());
        contact.setGid(orderForm.getGroupId());
        contact.setLatitude(orderForm.getLatitude());
        contact.setLongitude(orderForm.getLongitude());
        contact.setPhoneNum(orderForm.getServicePhone());
        contact.setServiceStartTime(orderInfo.getServiceStartTime());
        contact.setServiceAddress(orderForm.getServiceAddress());
        boolean create_contact = contactService.create(contact);
        log.info("CreateOrder -- orderId：{} save contact result：{}", orderId, create_contact);
        Request request = new Request();
        request.setRequestId(requestId);
        request.setServiceId(service.getServiceId());
        request.setRequestStatus(0);
        request.setOrderId(orderId);
        request.setCreateTime(now);
        request.setMerchantId(merchantId);
        request.setUpdateTime(now);
        boolean create_request = requestService.create(request);
        log.info("CreateOrder -- orderId：{} save requestId:{} result：{}", orderId, requestId, create_request);
        Order order = new Order();
        order.setMerchantId(merchantId);
        order.setOrderId(orderId);
        order.setPhone(orderForm.getServicePhone());
        order.setCreateTime(now);
        order.setCampaignId(1);
        order.setExtend("");
        order.setGid(orderForm.getGroupId());
        order.setGoodsId(orderForm.getGoodsId());
        order.setGoodsNum(1);
        order.setGoodsVersion(orderForm.getGoodsVersion());
        order.setMerchantId(merchantId);
        order.setOrderStatus((byte) OrderStatusEnum.OrderUnpaid.getValue());
        order.setPay(orderForm.getActPay());
        order.setPhone(orderForm.getServicePhone());
        order.setPrice(goods.getPrice());
        order.setRemark(orderForm.getRemark());
        order.setService_id(orderForm.getServiceId());
        order.setUpdateTime(now);
        order.setShOffSet(orderForm.getActOffset());
        order.setUserId(orderForm.getUserId());
        boolean create_order = orderService.createOrder(order);
        log.info("CreateOrder -- orderId：{} save order result：{}", orderId, create_order);
        return JSON.toJSONString(result);
    }
}
