package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.home.dao.RequestDao;
import com.shihui.openpf.home.dao.RequestHistoryDao;
import com.shihui.openpf.home.model.Request;
import com.shihui.openpf.home.model.RequestHistory;
import com.shihui.openpf.home.service.api.RequestService;

import me.weimi.api.commons.util.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by zhoutc on 2016/2/3.
 */
@Service
public class RequestServiceImpl implements RequestService {

    @Resource
    RequestDao requestDao;
    @Resource
    RequestHistoryDao requestHistoryDao;


    /**
     * 创建第三方订单
     * @param request  第三方订单信息
     * @return 创建结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(Request request) {
        RequestHistory requestHistory = new RequestHistory();
        Date now = new Date();
        requestHistory.setChangeTime(now);
        requestHistory.setRequestId(request.getRequestId());
        requestHistory.setRequestStatus(request.getRequestStatus());

        if(requestDao.save(request)>0){
            if(requestHistoryDao.save(requestHistory)>0)
                return true;
        }
        return false;
    }

    /**
     * 更新第三方订单
     * @param request 更新信息
     * @return 更新结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Request request) {
        RequestHistory requestHistory = new RequestHistory();
        Date now = new Date();
        requestHistory.setChangeTime(now);
        requestHistory.setRequestId(request.getRequestId());
        requestHistory.setRequestStatus(request.getRequestStatus());
        if(requestDao.update(request)>0){
            if(requestHistoryDao.save(requestHistory)>0)
                return true;
        }
        return false;
    }

    /**
     * 更新第三方订单状态
     * @param request 第三方订单ID
     * @return 更新结果
     */
    @Override
    public Request queryById(Request request) {
        return requestDao.findById(request);
    }

    @Override
    public Request queryOrderRequest(long orderId) {
        String sql = "select * from request where order_id = ? and request_status != -1";
        return requestDao.queryForObject(sql,new Object[]{orderId});
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateServiceStatus(String orderId, Integer status, String statusName, String serverName,
			String serverPhone, String comment) {
		Date now = new Date();
		RequestHistory requestHistory = new RequestHistory();
		requestHistory.setChangeTime(now);
		requestHistory.setComment(comment);
		requestHistory.setRequestId(orderId);
		requestHistory.setRequestStatus(status);
		requestHistory.setServerName(serverName);
		requestHistory.setServerPhone(serverPhone);
		requestHistory.setStatusName(statusName);
		requestHistoryDao.save(requestHistory);
		
		if(StringUtils.isNotBlank(serverName) || StringUtils.isNotBlank(serverPhone)){
			Request request = new Request();
			request.setRequestId(orderId);
			request.setUpdateTime(now);
			request.setServerName(serverName);
			request.setServerPhone(serverPhone);
			
			requestDao.update(request);
		}
	}
}
