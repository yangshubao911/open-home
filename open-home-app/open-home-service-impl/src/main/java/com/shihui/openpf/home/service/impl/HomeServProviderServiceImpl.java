/**
 * 
 */
package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantApi;
import com.shihui.openpf.common.model.MerchantApiName;
import com.shihui.openpf.common.service.api.GroupManage;
import com.shihui.openpf.home.api.ParamAssembler;
import com.shihui.openpf.home.api.ResultParser;
import com.shihui.openpf.home.http.FastHttpUtils;
import com.shihui.openpf.home.http.OpenHomeHttpCallbackHandler;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.model.OrderInfo;
import com.shihui.openpf.home.service.api.HomeServProviderService;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月15日 下午4:02:54
 */
@Service()
public class HomeServProviderServiceImpl implements HomeServProviderService{
	public static final String DEFAULT_ADAPTER = "defaultAdapter";
	public static final String DEFAULT_ENCODING = "utf8";
	public static final int WAIT_TIME_OUT = 5000;//请求等待时间，单位毫秒
	
	private static final int HTTP_GET = 1;
	private static final int HTTP_POST = 2;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	private CloseableHttpAsyncClient httpClient;
	private Map<String,ParamAssembler> paramAssemblerMap = new HashMap<>();
	private Map<String,ResultParser> resultParserMap = new HashMap<>();
	
	@Resource
	private GroupManage groupManage;
	@Resource
	private MerchantManage merchantManage;
	
	@PostConstruct
	public void init(){
		//初始化httpclient超时值为2s
		this.httpClient = FastHttpUtils.getClient(WAIT_TIME_OUT);
		//加载已实现的参数包装类
		ServiceLoader<ParamAssembler> paramAssemblers = ServiceLoader.load(ParamAssembler.class);
		Iterator<ParamAssembler> pa_it = paramAssemblers.iterator();
		while(pa_it.hasNext()){
			ParamAssembler pa = pa_it.next();
			paramAssemblerMap.put(pa.getAdapterName(), pa);
		}
		//加载已实现的结果解析类
		ServiceLoader<ResultParser> resultParsers = ServiceLoader.load(ResultParser.class);
		Iterator<ResultParser> rp_it = resultParsers.iterator();
		while(rp_it.hasNext()){
			ResultParser rp = rp_it.next();
			resultParserMap.put(rp.getAdapterName(), rp);
		}
	}
	
	@Override
	public HomeResponse getServiceAvailableTime(int serviceType, int cityId, String longitude, String latitude, List<Merchant> merchants, int categoryId , int amount, String productId) {
		//查询供应商信息
		Map<Merchant, MerchantApi> merchantApiMap = this.merchantManage.getMerchantAvailableTimeApi(serviceType, merchants);
		if(merchantApiMap == null || merchantApiMap.size() == 0){
			HomeResponse response = new HomeResponse();
			response.setCode(1004);
			response.setMsg("抱歉，本地点没有提供服务的供应商");
			
			return response;
		}
		//请求供应商接口
		List<OpenHomeHttpCallbackHandler<String>> handlers = new LinkedList<OpenHomeHttpCallbackHandler<String>>();
		for(Entry<Merchant, MerchantApi> entry : merchantApiMap.entrySet()){
			if(entry.getKey() == null){
				continue;
			}
			MerchantApi api = entry.getValue();
			ParamAssembler paramParser = null;
			ResultParser resultParser = null;

			if(api==null) {
				log.info("商户位配置获取服务时间接口，merchant_id={}" , entry.getKey().getMerchantId());
				continue;
			}
			//获得对应的参数组装器与结果解析器，如果未配置则用默认实现，即标准化实现
			if(api.getAdapterName() == null || api.getAdapterName().isEmpty()){
				paramParser = this.paramAssemblerMap.get(DEFAULT_ADAPTER);
				resultParser = this.resultParserMap.get(DEFAULT_ADAPTER);
			}else {
				paramParser = this.paramAssemblerMap.get(api.getAdapterName());
				resultParser = this.resultParserMap.get(api.getAdapterName());
			}
			//执行请求，非阻塞方式
			OpenHomeHttpCallbackHandler<String> handler = new OpenHomeHttpCallbackHandler<String>(entry.getKey(), resultParser);
			handlers.add(handler);
			Map<String, String> param = paramParser.getServiceAvailableTimeParam(entry.getKey(), serviceType, cityId, longitude, latitude, api.getVersion(),categoryId,amount, productId);
		    this.executeHttpRequst(api, param, handler, HTTP_GET);
		}
		//解析并合并结果
		HomeResponse response = new HomeResponse();
		response.setCode(0);
		response.setMsg("查询成功");
		long start = System.currentTimeMillis();
		JSONArray resultArray = new JSONArray();

		while(System.currentTimeMillis() - start < WAIT_TIME_OUT){
			int size = handlers.size();
			for(int i = size -1; i >= 0; i-- ){
				OpenHomeHttpCallbackHandler<String> handler= handlers.get(i);
				if(handler.isSuccess()){
					//处理请求结果
					String content = handler.get();
					HomeResponse responseTmp = handler.getResultParser().getServiceAvailableTimeResult(handler.getMerchant(), content);
					log.info("请求服务时间接口完成, response={}", responseTmp);
					if(responseTmp.getCode() == 0){
						JSONObject result = JSONObject.parseObject(responseTmp.getResult());
						//加入商户id以便区分
						result.put("merchant_id", handler.getMerchant().getMerchantId());
						
						resultArray.add(result);
					}
					//删除已经处理完的请求
					handlers.remove(i);
				}
			}
			if(handlers.size() == 0)
				break;
		}
		if(handlers.size() != 0){
			List<Integer> merchantIdList = new ArrayList<>(handlers.size());
			for(OpenHomeHttpCallbackHandler<String> handler : handlers){
				merchantIdList.add(handler.getMerchant().getMerchantId());
			}
			log.warn("接口请求超时供应商={}", merchantIdList);
		}
		response.setResult(resultArray.toJSONString());
		return response;
	}

	@Override
	public HomeResponse isServiceAvailable(Merchant merchant, int serviceType, int goodsId, long gid, String longitude,
			String latitude, String serviceStartTime, int categoryId , int amount , String productId) {
		//查询小区信息
		Group group = this.groupManage.getGroupInfoByGid(gid);
		if(group == null){
			HomeResponse response = new HomeResponse();
			response.setCode(-1);
			response.setMsg("小区未开通服务或者小区信息不存在");
			
			return response;
		}
		MerchantApi api = this.merchantManage.getMerchantApi(serviceType, merchant.getMerchantId(), MerchantApiName.IS_SERVICE_AVAILABLE);
		if(api == null){
			HomeResponse response = new HomeResponse();
			response.setCode(2004);
			response.setMsg("接口不存在");
			
			return response;
		}
		ParamAssembler paramParser = null;
		ResultParser resultParser = null;
		//获得对应的参数组装器与结果解析器，如果未配置则用默认实现，即标准化实现
		if(api.getAdapterName() == null || api.getAdapterName().isEmpty()){
			paramParser = this.paramAssemblerMap.get(DEFAULT_ADAPTER);
			resultParser = this.resultParserMap.get(DEFAULT_ADAPTER);
		}else {
			paramParser = this.paramAssemblerMap.get(api.getAdapterName());
			resultParser = this.resultParserMap.get(api.getAdapterName());
		}
		//执行请求，非阻塞方式
		OpenHomeHttpCallbackHandler<String> handler = new OpenHomeHttpCallbackHandler<String>(merchant, resultParser);
		Map<String, String> param = paramParser.isServiceAvailableParam(merchant, serviceType, group.getCityId(), longitude, latitude,
				serviceStartTime, api.getVersion(), categoryId , amount, productId);
		this.executeHttpRequst(api, param, handler, HTTP_GET);
		String content = handler.get();
		log.info("请求第三方接口完成, url={}, param={}, response={}", api.getApiUrl(), param, content);
		if(content == null || content.isEmpty()){
			HomeResponse response = new HomeResponse();
			response.setCode(1004);
			response.setMsg("请求接口失败");
			return response;
		}
		HomeResponse response = handler.getResultParser().isServiceAvailableResult(merchant, content);
		
		return response;
	}

	@Override
	public HomeResponse createOrder(Merchant merchant, int serviceType, OrderInfo orderInfo) {
		StringBuilder logs = new StringBuilder();
		logs.append("\r\n merchantId:" + merchant.getMerchantId());
		logs.append("\r\n merchantName:"+merchant.getMerchantName());
		logs.append("\r\n merchantImage:"+merchant.getMerchantImage());
		logs.append("\r\n merchantStatus:"+merchant.getMerchantStatus());
		logs.append("\r\n merchantDesc:"+merchant.getMerchantDesc());
		logs.append("\r\n merchantLink:"+merchant.getMerchantLink());
		logs.append("\r\n merchantCode:"+merchant.getMerchantCode());
		logs.append("\r\n merchantKey:"+merchant.getMerchantKey());
		logs.append("\r\n md5Key:"+merchant.getMd5Key());

		logs.append("\r\n serviceType:" + serviceType);

		logs.append("\r\n cityId;" + orderInfo.getCityId());
		logs.append("\r\n serviceAddress;" + orderInfo.getServiceAddress());
		logs.append("\r\n detailAddress;" + orderInfo.getDetailAddress());
		logs.append("\r\n longitude;" + orderInfo.getLongitude());
		logs.append("\r\n latitude;" + orderInfo.getLatitude());
		logs.append("\r\n phone;" + orderInfo.getPhone());
		logs.append("\r\n price;" + orderInfo.getPrice());
		logs.append("\r\n offSet;" + orderInfo.getOffSet());
		logs.append("\r\n serviceStartTime;" + orderInfo.getServiceStartTime());
		logs.append("\r\n contactName;" + orderInfo.getContactName());
		logs.append("\r\n amount;" + orderInfo.getAmount());
		logs.append("\r\n goodsId;" + orderInfo.getGoodsId());
		logs.append("\r\n remark;" + orderInfo.getRemark());
		logs.append("\r\n productId;" + orderInfo.getProductId());
		logs.append("\r\n extend;" + orderInfo.getExtend());
		try {
			FileCopyUtils.copy(logs.toString().getBytes(), new File("/tmp/open-home1.txt"));
		} catch (IOException e) {
		}

		MerchantApi api = this.merchantManage.getMerchantApi(serviceType, merchant.getMerchantId(), MerchantApiName.CREATE_ORDER);
		if(api == null){
			HomeResponse response = new HomeResponse();
			response.setCode(0);
			response.setMsg("接口不存在");
			try {
				FileCopyUtils.copy("接口不存在".getBytes(), new File("/tmp/open-home2.txt"));
			} catch (IOException e) {
			}
			return response;
		}
		logs.append("\r\n merchantId;" + api.getMerchantId());
		logs.append("\r\n serviceId; " + api.getServiceId());
		logs.append("\r\n apiType;" + api.getApiType());
		logs.append("\r\n apiName;" + api.getApiName());
		logs.append("\r\n apiUrl;" + api.getApiUrl());
		logs.append("\r\n status;" + api.getStatus());
		logs.append("\r\n adapterName;" + api.getAdapterName());
		logs.append("\r\n version;" + api.getVersion());
		logs.append("\r\n httpMethod;" + api.getHttpMethod());
		try {
			FileCopyUtils.copy(logs.toString().getBytes(), new File("/tmp/open-home3.txt"));
		} catch (IOException e) {
		}

		ParamAssembler paramParser = null;
		ResultParser resultParser = null;
		//获得对应的参数组装器与结果解析器，如果未配置则用默认实现，即标准化实现
		if(api.getAdapterName() == null || api.getAdapterName().isEmpty()){
			paramParser = this.paramAssemblerMap.get(DEFAULT_ADAPTER);
			resultParser = this.resultParserMap.get(DEFAULT_ADAPTER);
		}else {
			paramParser = this.paramAssemblerMap.get(api.getAdapterName());
			resultParser = this.resultParserMap.get(api.getAdapterName());
		}
		//执行请求，非阻塞方式
		OpenHomeHttpCallbackHandler<String> handler = new OpenHomeHttpCallbackHandler<String>(merchant, resultParser);
		Map<String, String> param = paramParser.createOrderParam(merchant, serviceType, orderInfo, api.getVersion());
		this.executeHttpRequst(api, param, handler, HTTP_POST);
		String content = handler.get();
		log.info("请求第三方接口完成, url={}, param={}, response={}", api.getApiUrl(), param, content);

		try {
			logs.append("\r\nurl:" + api.getApiUrl() );
			logs.append("\r\nparam:" + param);
			logs.append("\r\ncontent:" + content);
			FileCopyUtils.copy(logs.toString().getBytes(), new File("/tmp/open-home4.txt"));
		} catch (IOException e) {
		}
		if(content == null || content.isEmpty()){
			HomeResponse response = new HomeResponse();
			response.setCode(1004);
			response.setMsg("请求接口失败");
			return response;
		}
		HomeResponse response = handler.getResultParser().createOrderResult(merchant, content);
		return response;
	}

	@Override
	public HomeResponse cancelOrder(Merchant merchant, int serviceType, String orderId) {
		MerchantApi api = this.merchantManage.getMerchantApi(serviceType, merchant.getMerchantId(), MerchantApiName.CANCEL_ORDER);
		if(api == null){
			HomeResponse response = new HomeResponse();
			response.setCode(2004);
			response.setMsg("接口不存在");
			
			return response;
		}
		ParamAssembler paramParser = null;
		ResultParser resultParser = null;
		//获得对应的参数组装器与结果解析器，如果未配置则用默认实现，即标准化实现
		if(api.getAdapterName() == null || api.getAdapterName().isEmpty()){
			paramParser = this.paramAssemblerMap.get(DEFAULT_ADAPTER);
			resultParser = this.resultParserMap.get(DEFAULT_ADAPTER);
		}else {
			paramParser = this.paramAssemblerMap.get(api.getAdapterName());
			resultParser = this.resultParserMap.get(api.getAdapterName());
		}
		//执行请求，非阻塞方式
		OpenHomeHttpCallbackHandler<String> handler = new OpenHomeHttpCallbackHandler<String>(merchant, resultParser);
		Map<String, String> param = paramParser.cancelOrderParam(merchant, serviceType, orderId, api.getVersion());
		this.executeHttpRequst(api, param, handler, HTTP_POST);
		String content = handler.get();
		log.info("请求第三方接口完成, url={}, param={}, response={}", api.getApiUrl(), param, content);
		if(content == null || content.isEmpty()){
			HomeResponse response = new HomeResponse();
			response.setCode(1004);
			response.setMsg("请求接口失败");
			return response;
		}
		HomeResponse response = handler.getResultParser().cancelOrderResult(merchant, content);
		return response;
	}

	@Override
	public HomeResponse payNotice(Merchant merchant, int serviceType, String orderId, String settlePrice) {
		MerchantApi api = this.merchantManage.getMerchantApi(serviceType, merchant.getMerchantId(), MerchantApiName.PAY_NOTICE);
		if(api == null){
			HomeResponse response = new HomeResponse();
			response.setCode(2004);
			response.setMsg("接口不存在");
			
			return response;
		}
		ParamAssembler paramParser = null;
		ResultParser resultParser = null;
		//获得对应的参数组装器与结果解析器，如果未配置则用默认实现，即标准化实现
		if(api.getAdapterName() == null || api.getAdapterName().isEmpty()){
			paramParser = this.paramAssemblerMap.get(DEFAULT_ADAPTER);
			resultParser = this.resultParserMap.get(DEFAULT_ADAPTER);
		}else {
			paramParser = this.paramAssemblerMap.get(api.getAdapterName());
			resultParser = this.resultParserMap.get(api.getAdapterName());
		}
		//执行请求，非阻塞方式
		OpenHomeHttpCallbackHandler<String> handler = new OpenHomeHttpCallbackHandler<String>(merchant, resultParser);
		Map<String, String> param = paramParser.payNoticeParam(merchant, serviceType, orderId, settlePrice, api.getVersion());
		this.executeHttpRequst(api, param, handler, HTTP_POST);
		String content = handler.get();
		log.info("请求第三方接口完成, url={}, param={}, response={}", api.getApiUrl(), param, content);
		if(content == null || content.isEmpty()){
			HomeResponse response = new HomeResponse();
			response.setCode(1004);
			response.setMsg("请求接口失败");
			return response;
		}
		HomeResponse response = handler.getResultParser().payNoticeResult(merchant, content);
		return response;
	}

	@Override
	public HomeResponse evaluateOrder(Merchant merchant, int serviceType, String orderId, int score, String comments) {
		MerchantApi api = this.merchantManage.getMerchantApi(serviceType, merchant.getMerchantId(), MerchantApiName.EVALUATE_ORDER);
		if(api == null){
			HomeResponse response = new HomeResponse();
			response.setCode(2004);
			response.setMsg("接口不存在");
			
			return response;
		}
		ParamAssembler paramParser = null;
		ResultParser resultParser = null;
		//获得对应的参数组装器与结果解析器，如果未配置则用默认实现，即标准化实现
		if(api.getAdapterName() == null || api.getAdapterName().isEmpty()){
			paramParser = this.paramAssemblerMap.get(DEFAULT_ADAPTER);
			resultParser = this.resultParserMap.get(DEFAULT_ADAPTER);
		}else {
			paramParser = this.paramAssemblerMap.get(api.getAdapterName());
			resultParser = this.resultParserMap.get(api.getAdapterName());
		}
		//执行请求，非阻塞方式
		OpenHomeHttpCallbackHandler<String> handler = new OpenHomeHttpCallbackHandler<String>(merchant, resultParser);
		Map<String, String> param = paramParser.evaluateOrderParam(merchant, serviceType, orderId, score, comments, api.getVersion());
		this.executeHttpRequst(api, param, handler, HTTP_POST);
		String content = handler.get();
		log.info("请求第三方接口完成, url={}, param={}, response={}", api.getApiUrl(), param, content);
		if(content == null || content.isEmpty()){
			HomeResponse response = new HomeResponse();
			response.setCode(1004);
			response.setMsg("请求接口失败");
			return response;
		}
		HomeResponse response = handler.getResultParser().evaluateOrderResult(merchant, content);
		return response;
	}
	
	/**
	 * 执行http method
	 * @param api
	 * @param param
	 * @param handler
	 * @param defaultMethod
	 */
	private void executeHttpRequst(MerchantApi api, Map<String, String> param, OpenHomeHttpCallbackHandler<String> handler, int defaultMethod){
		int httpMethod = api.getHttpMethod();
		if(httpMethod == 0){
			httpMethod = defaultMethod;
		}
		if(httpMethod == HTTP_GET){//http get
			try {
				StringBuilder url = new StringBuilder(api.getApiUrl());
				if(url.indexOf("?") <0){
					url.append("?");
				}
				for(Entry<String, String> entry2 : param.entrySet()){
					url.append(URLEncoder.encode(entry2.getKey(), "utf-8")).append("=").append(URLEncoder.encode(entry2.getValue(), "utf-8")).append("&");
				}
				if(url.length() > 0){
					url.deleteCharAt(url.length() - 1);
				}
				HttpGet httpGet = new HttpGet(url.toString());

				FastHttpUtils.executeHttpGetReturnString(httpClient, httpGet, DEFAULT_ENCODING, handler, false);
			} catch (Exception e) {
				log.error("请求供应商接口异常，url={}", api.getApiUrl(), e);
			}
		}else{// http post
			HttpPost httpPost = new HttpPost(api.getApiUrl());
			try {
				FastHttpUtils.executeHttpPostReturnString(httpClient, httpPost, param, DEFAULT_ENCODING, handler, false);
			} catch (Exception e) {
				log.error("请求供应商接口异常，url={}", api.getApiUrl(), e);
			}
		}
		log.info("请求第三方接口, url={}, param={}, method={}", api.getApiUrl(), param, httpMethod == HTTP_GET ? "get" : "post");
	}

}
