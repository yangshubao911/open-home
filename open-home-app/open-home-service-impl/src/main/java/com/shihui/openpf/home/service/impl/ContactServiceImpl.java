package com.shihui.openpf.home.service.impl;

import com.shihui.openpf.home.dao.ContactDao;
import com.shihui.openpf.home.model.Contact;
import com.shihui.openpf.home.service.api.ContactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/2/2.
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Resource
    ContactDao contactDao;

    /**
     * 创建联系人信息
     * @param contact 联系人信息
     * @return 更新结果
     */
    @Override
    public boolean create(Contact contact) {
        return contactDao.insert(contact)>0;
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
}
