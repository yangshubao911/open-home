/**
 * 
 */
package com.shihui.openpf.home.resource;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.common.tools.StringUtil;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.service.api.OrderManage;
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
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月19日 下午3:00:15
 */
@Controller
@Path("/v2/openpf/home/order")
public class OrderResource {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	OrderManage orderManage;

	
	@Path("/listById")
	@GET
	@BaseInfo(desc = "根据条件查询订单列表", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String listById(@Context RequestContext rc,
						   @ParamDesc(desc = "实惠用户id", isRequired = false) @QueryParam("userId") String userId,
						   @ParamDesc(desc = "业务id", isRequired = false) @QueryParam("serviceId") String serviceId,
						   @ParamDesc(desc = "充值手机号码", isRequired = false) @QueryParam("phoneNum") String phoneNum,
						   @ParamDesc(desc = "商户id", isRequired = false) @QueryParam("merchantId") String merchantId,
						   @ParamDesc(desc = "订单id", isRequired = false) @QueryParam("orderId") String orderId,
						   @ParamDesc(desc = "订单状态", isRequired = false) @QueryParam("status") String status,
						   @ParamDesc(desc = "开始时间", isRequired = false) @QueryParam("startTime") String startTime,
						   @ParamDesc(desc = "结束时间", isRequired = false) @QueryParam("endTime") String endTime,
						   @ParamDesc(desc = "服务社id", isRequired = false) @QueryParam("mid") String mid,
						   @ParamDesc(desc = "页标", isRequired = false) @QueryParam("page") @DefaultValue("1") int page,
						   @ParamDesc(desc = "每页显示数量", isRequired = false) @QueryParam("size") @DefaultValue("10") int size) {

		Order queryOrder = new Order();
		try {

			if (!StringUtil.isEmpty(orderId))
				queryOrder.setOrderId(Long.parseLong(orderId));
			if (!StringUtil.isEmpty(status))
				queryOrder.setOrderStatus(Integer.parseInt(status));
			if (!StringUtil.isEmpty(phoneNum))
				queryOrder.setPhone(phoneNum);
			if (!StringUtil.isEmpty(userId))
				queryOrder.setUserId(Long.parseLong(userId));
			if (!StringUtil.isEmpty(merchantId))
				queryOrder.setMerchantId(Integer.parseInt(merchantId));
			if (!StringUtil.isEmpty(serviceId))
				queryOrder.setService_id(Integer.parseInt(serviceId));
			if (!StringUtil.isEmpty(mid))
				queryOrder.setMid(Long.parseLong(mid));
			return orderManage.queryOrderList(queryOrder, startTime, endTime, page, size);
		} catch (Exception e) {
			log.error("查询订单列表异常，param={}", JSON.toJSONString(queryOrder), e);
		}
		return null;
	}


	@Path("/export")
	@GET
	@BaseInfo(desc = "根据条件查询订单列表", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
	@Produces({ MediaType.APPLICATION_JSON })
	public String export(@Context RequestContext rc,
						   @ParamDesc(desc = "实惠用户id", isRequired = false) @QueryParam("userId") String userId,
						   @ParamDesc(desc = "业务id", isRequired = false) @QueryParam("serviceId") String serviceId,
						   @ParamDesc(desc = "充值手机号码", isRequired = false) @QueryParam("phoneNum") String phoneNum,
						   @ParamDesc(desc = "商户id", isRequired = false) @QueryParam("merchantId") String merchantId,
						   @ParamDesc(desc = "订单id", isRequired = false) @QueryParam("orderId") String orderId,
						   @ParamDesc(desc = "订单状态", isRequired = false) @QueryParam("status") String status,
						   @ParamDesc(desc = "开始时间", isRequired = false) @QueryParam("startTime") String startTime,
						 @ParamDesc(desc = "服务社id", isRequired = false) @QueryParam("mid") String mid,
						 @ParamDesc(desc = "结束时间", isRequired = false) @QueryParam("endTime") String endTime) {

		Order queryOrder = new Order();
		try {

			if (!StringUtil.isEmpty(orderId))
				queryOrder.setOrderId(Long.parseLong(orderId));
			if (!StringUtil.isEmpty(status))
				queryOrder.setOrderStatus(Integer.parseInt(status));
			if (!StringUtil.isEmpty(phoneNum))
				queryOrder.setPhone(phoneNum);
			if (!StringUtil.isEmpty(userId))
				queryOrder.setUserId(Long.parseLong(userId));
			if (!StringUtil.isEmpty(merchantId))
				queryOrder.setMerchantId(Integer.parseInt(merchantId));
			if (!StringUtil.isEmpty(serviceId))
				queryOrder.setService_id(Integer.parseInt(serviceId));
			if (!StringUtil.isEmpty(mid))
				queryOrder.setMid(Long.parseLong(mid));
			return orderManage.exportOrderList(queryOrder, startTime, endTime);
		} catch (Exception e) {
			log.error("查询订单列表异常，param={}", JSON.toJSONString(queryOrder), e);
		}
		return null;
	}

	@GET
	@Path("/detail")
	@BaseInfo(desc = "查询订单详情", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
	public String detail(@Context RequestContext rc,
			@ParamDesc(isRequired = true, desc = "订单ID") @QueryParam("orderId") long orderId) {
		return orderManage.queryOrder(orderId);
	}

	@GET
	@Path("/thirdOrder/detail")
	@Produces({ MediaType.TEXT_HTML })
	@BaseInfo(desc = "查询第三方订单详情", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
	public String thirdOrderDetail(@Context RequestContext rc,
			@ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
			@ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
			@ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
			@ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
			@ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign) {
		return orderManage.queryThirdOrder(key, serviceType, orderId, version, sign);
	}

	@POST
	@Path("/thirdOrder/cancel")
	@Produces({ MediaType.TEXT_HTML })
	@BaseInfo(desc = "第三方取消订单接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
	public String thirdOrderCancel(@Context RequestContext rc,
			@ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
			@ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
			@ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
			@ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
			@ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign) {
		return orderManage.cancelThirdOrder(key, serviceType, orderId, version, sign);
	}

	@POST
	@Path("/thirdOrder/update")
	@Produces({ MediaType.TEXT_HTML })
	@BaseInfo(desc = "第三方更新订单接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
	public String thirdOrderUpdate(@Context RequestContext rc,
			@ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
			@ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
			@ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
			@ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
			@ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign,
			@ParamDesc(isRequired = true, desc = "订单状态") @QueryParam("status") int status) {
		return orderManage.updateThirdOrder(key, serviceType, orderId, version, sign, status);
	}
	
	@POST
	@Path("/thirdOrder/updateServiceStatus")
	@Produces({ MediaType.TEXT_HTML })
	@BaseInfo(desc = "第三方更新服务状态接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
	public String thirdOrderUpdateServiceStatus(@Context RequestContext rc,
			@ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
			@ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
			@ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
			@ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
			@ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign,
			@ParamDesc(isRequired = false, desc = "订单状态") @QueryParam("status") Integer status,
			@ParamDesc(isRequired = true, desc = "订单状态描述") @QueryParam("statusName") String statusName,
			@ParamDesc(isRequired = false, desc = "服务人员姓名") @QueryParam("serverName") String serverName,
			@ParamDesc(isRequired = false, desc = "服务人员联系方式") @QueryParam("serverPhone") String serverPhone,
			@ParamDesc(isRequired = false, desc = "备注") @QueryParam("comment") String comment) {
		return orderManage.updateThirdOrderServiceStatus(key, serviceType, orderId, version, sign, status, statusName, serverName, serverPhone, comment);
	}
	
	@POST
	@Path("/thirdOrder/updateServiceStartTime")
	@Produces({ MediaType.TEXT_HTML })
	@BaseInfo(desc = "第三方更新服务开始时间接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
	public String thirdOrderUpdateServiceStartTime(@Context RequestContext rc,
			@ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
			@ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
			@ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
			@ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
			@ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign,
			@ParamDesc(isRequired = true, desc = "服务开始时间") @QueryParam("serviceStartTime") String serviceStartTime,
			@ParamDesc(isRequired = false, desc = "备注") @QueryParam("comment") String comment) {
		return orderManage.updateThirdOrderServiceStartTime(key, serviceType, orderId, version, sign, serviceStartTime, comment);
	}

	@GET
	@Path("/cancel")
	@BaseInfo(desc = "取消订单接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
	public String cancel(@Context RequestContext rc,
						 @ParamDesc(isRequired = true, desc = "操作人ID") @QueryParam("userId") long userId,
						 @ParamDesc(isRequired = true, desc = "操作人Email") @QueryParam("email") String email,
						 @ParamDesc(isRequired = true, desc = "订单ID") @QueryParam("orderId") long orderId,
						 @ParamDesc(isRequired = true, desc = "退款金额") @QueryParam("price") String price,
						 @ParamDesc(isRequired = true, desc = "退款备注") @QueryParam("reason") String reason,
						 @ParamDesc(isRequired = true, desc = "当前状态") @QueryParam("status") int status,
						 @ParamDesc(isRequired = true, desc = "是否退实惠现金，1-是，2-否") @QueryParam("refund_sh_coin") Integer refundSHCoin) {
		return orderManage.cancelLocalOrder(userId, email, orderId, price, reason, refundSHCoin, status);
	}

	@GET
	@Path("/unusualOrder/count")
	@BaseInfo(desc = "异常订单数量查询接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
	public String count(@Context RequestContext rc) {
		return orderManage.countunusual();
	}

	@GET
	@Path("/unusualOrder/query")
	@BaseInfo(desc = "异常订单查询接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
	public String query(@Context RequestContext rc) {
		return orderManage.queryUnusual();
	}

	@GET
	@Path("/unusualOrder/export")
	@Produces("application/vnd.ms-excel; charset=UTF-8")
	@BaseInfo(desc = "异常订单导出接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
	public Response export(@Context RequestContext rc) {
		String fileName = orderManage.exportUnusual();
		File file = new File(fileName);
		Response.ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=\"unusualOrder.xlsx\"");
		response.header("content-transfer-encoding", "binary");

		return response.build();
	}

	@POST
	@Path("/yjz/updateOrderStatus")
	@Produces(MediaType.TEXT_HTML)
	@BaseInfo(desc = "云家政更新订单状态接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION, crossDomain = true)
	public String updateOrderStatus(@Context RequestContext rc,
			@ParamDesc(desc = "第三方订单Id", isRequired = true) @QueryParam("orderId") String orderId,
			@ParamDesc(desc = "第三方订单状态", isRequired = true) @QueryParam("status") int status,
			@ParamDesc(desc = "pid", isRequired = false) @QueryParam("pId") String pId,
			@ParamDesc(desc = "接口版本", isRequired = false) @QueryParam("version") String version,
			@ParamDesc(desc = "请求方法", isRequired = false) @QueryParam("methodName") String methodName,
			@ParamDesc(desc = "签名", isRequired = true) @QueryParam("sign") String sign

	) {

		return orderManage.yjzConverter(orderId, status, pId, version, methodName, sign);
	}

}
