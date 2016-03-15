/**
 * 
 */
package com.shihui.openpf.home.service.api;

import java.util.List;

import com.shihui.openpf.home.model.OrderBad;

/**
 * @author zhouqisheng
 * @date 2016年3月14日 下午2:17:36
 *
 */
public interface OrderBadService {
	
	boolean save(OrderBad orderBad);
	
	boolean update(OrderBad orderBad);
	
	boolean delete(OrderBad orderBad);
	
	List<OrderBad> querByCondition(OrderBad orderBad);

}
