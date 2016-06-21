package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.home.dao.ContactDao;
import com.shihui.openpf.home.dao.RequestHistoryDao;
import com.shihui.openpf.home.model.Contact;
import com.shihui.openpf.home.model.Request;
import com.shihui.openpf.home.model.RequestHistory;
import com.shihui.openpf.home.service.api.ContactService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/2/2.
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Resource
    ContactDao contactDao;
    @Resource
    RequestHistoryDao requestHistoryDao;

    /**
     * 创建联系人信息
     * @param contact 联系人信息
     * @return 更新结果
     */
    @Override
    public boolean create(Contact contact) {
        return contactDao.save(contact)>0;
    }

    /**
     * 更新联系人信息
     * @param contact 联系人信息
     * @return 更新结果
     */
    @Override
    public boolean update(Contact contact) {
        return contactDao.update(contact)>0;
    }

    /**
     * 查询联系人信息
     * @param orderId 订单id
     * @return 联系人信息
     */
    @Override
    public Contact queryByOrderId(long orderId) {
        String sql = "select * from contact where order_id = ?";
        return contactDao.queryForObject(sql,orderId);
    }
    @Transactional(rollbackFor = Exception.class)
	public void updateServiceStartTime(String orderId, String serviceStartTime, String comment, Request db_request) {
		Date now = new Date();
		RequestHistory requestHistory = new RequestHistory();
		requestHistory.setChangeTime(now);
		requestHistory.setComment(comment);
		requestHistory.setRequestId(orderId);
		requestHistory.setServiceStartTime(serviceStartTime);
		requestHistoryDao.save(requestHistory);
		
		Contact contact = new Contact();
		contact.setOrderId(db_request.getOrderId());
		contact.setServiceStartTime(serviceStartTime);
		
		contactDao.update(contact);
	}
}
