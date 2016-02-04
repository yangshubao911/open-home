package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.Contact;

/**
 * Created by zhoutc on 2016/2/2.
 */
public interface ContactService {

    /**
     * 创建联系人信息
     * @param contact 联系人信息
     * @return 更新结果
     */
    public boolean create(Contact contact);

    /**
     * 更新联系人信息
     * @param contact 联系人信息
     * @return 更新结果
     */
    public boolean update(Contact contact);

    /**
     * 查询联系人信息
     * @param orderId 订单id
     * @return 联系人信息
     */
    public Contact queryByOrderId(long orderId);

}
