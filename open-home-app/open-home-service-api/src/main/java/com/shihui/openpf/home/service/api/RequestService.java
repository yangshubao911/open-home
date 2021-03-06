package com.shihui.openpf.home.service.api;

import com.shihui.openpf.home.model.Request;

/**
 * Created by zhoutc on 2016/2/3.
 */
public interface RequestService {

    /**
     * 创建第三方订单
     * @param request  第三方订单信息
     * @return 创建结果
     */
    public boolean create(Request request);

    /**
     * 更新第三方订单状态
     * @param request 更新信息
     * @return 更新结果
     */
    public boolean updateStatus(Request request);

    /**
     * 查询第三方订单状态
     * @param request 第三方订单ID
     * @return 更新结果
     */
    public Request queryById(Request request);

    /**
     * 查询第三方订单状态
     * @param orderId 订单Id
     * @return 更新结果
     */
    public Request queryOrderRequest(long orderId);
    
    /**
     * 更新服务状态
     * @param orderId
     * @param status
     * @param statusName
     * @param serverName
     * @param serverPhone
     * @param comment
     */
    public void updateServiceStatus(String orderId, Integer status, String statusName, String serverName,
			String serverPhone, String comment);
}
