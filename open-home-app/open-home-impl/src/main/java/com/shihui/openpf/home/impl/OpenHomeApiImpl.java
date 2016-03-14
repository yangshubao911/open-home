package com.shihui.openpf.home.impl;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.vo.SimpleResult;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.tools.StringUtil;
import com.shihui.openpf.home.api.OpenHomeApi;
import com.shihui.openpf.home.api.SimpleHomeResponse;
import com.shihui.openpf.home.model.Contact;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.Request;
import com.shihui.openpf.home.service.api.ContactService;
import com.shihui.openpf.home.service.api.HomeServProviderService;
import com.shihui.openpf.home.service.api.OrderService;
import com.shihui.openpf.home.service.api.OrderSystemService;
import com.shihui.openpf.home.service.api.RequestService;

/**
 * @author zhouqisheng
 * @date 2016年3月11日 下午7:42:57
 *
 */
@Service
public class OpenHomeApiImpl implements OpenHomeApi {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Resource(name = "openOrderService")
	OrderService orderService;
	@Resource
	RequestService requestService;
	@Resource
	ContactService contactService;
	@Resource
	MerchantManage merchantManage;
	@Resource
	HomeServProviderService homeServProviderService;
	@Resource
	OrderSystemService orderSystemService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shihui.openpf.home.api.OpenHomeApi#userCancelOrder(long, long)
	 */
	@Override
	public SimpleHomeResponse userCancelOrder(long orderId, long userId, String reason) {
		SimpleHomeResponse response = new SimpleHomeResponse();
		try {
			Order order = orderService.queryOrder(orderId);
			if (order == null) {
				response.setCode(1);
				response.setMsg("订单不存在");
				return response;
			}
			OrderStatusEnum orderStatus = OrderStatusEnum.parse(order.getOrderStatus());
			if (orderStatus == OrderStatusEnum.BackClose 
					|| orderStatus == OrderStatusEnum.OrderCancelByCustom
					|| orderStatus == OrderStatusEnum.OrderCloseByOutTime
					|| orderStatus == OrderStatusEnum.OrderHadReceived 
					|| orderStatus == OrderStatusEnum.PayedCancel
					|| orderStatus == OrderStatusEnum.BackClose) {
				response.setCode(1);
				response.setMsg("订单已经关闭，不允许再次关闭");
				return response;
			} else if(orderStatus == OrderStatusEnum.OrderDistribute){
				Contact contact = contactService.queryByOrderId(orderId);
				if (contact == null) {
					response.setCode(1);
					response.setMsg("订单信息缺失");
					return response;
				}
				
				//判断预约时间差
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				Date startTime = sdf.parse(contact.getServiceStartTime());
				long diffTime = startTime.getTime() - System.currentTimeMillis() ;
				if(diffTime <= 2 * 60 * 60 * 1000){
					response.setCode(1);
					response.setMsg("预约时间已临近，不允许取消订单");
					return response;
				}
			}

			Request request = requestService.queryOrderRequest(orderId);
			if (request == null) {
				response.setCode(1);
				response.setMsg("订单信息缺失");
				return response;
			}
			Merchant merchant = merchantManage.getById(order.getMerchantId());
			if (merchant == null) {
				response.setCode(1);
				response.setMsg("商户不存在");
				return response;
			}
			HomeResponse homeResponse = homeServProviderService.cancelOrder(merchant, order.getService_id(),
					request.getRequestId());
			if (homeResponse.getCode() == 0) {
				// 商户取消订单，全额退款，无需审核，退回实惠现金
				SimpleResult sr = orderSystemService.customCancel(order.getOrderId(), merchant.getMerchantCode(),
						userId, StringUtil.yuan2hao(order.getPrice()), order.getOrderStatus(), reason);
				if (sr.getStatus() == 1) {
					// 保存审核id
					Order updateOrder = new Order();
					updateOrder.setOrderId(order.getOrderId());
					updateOrder.setAuditId((long) sr.getData());
					updateOrder.setUpdateTime(new Date());
					this.orderService.update(order);

					response.setCode(0);
					response.setMsg("取消订单成功");
					return response;
				} else {
					log.error("用户取消订单并发起退款失败，订单号={}，用户id={}，原订单状态={}", order.getOrderId(), userId,
							order.getOrderStatus());
					response.setCode(1);
					response.setMsg("调用订单系统接口失败，msg=" + sr.getMsg());
					return response;
				}
			} else {
				response.setCode(1);
				response.setMsg(homeResponse.getMsg());
				return response;
			}
		} catch (Exception e) {
			log.error("用户取消订单接口异常，order={}", orderId, e);
			response.setCode(1);
			response.setMsg("系统错误");
			return response;
		}
	}

}
