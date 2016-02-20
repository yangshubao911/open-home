/**
 *
 */
package com.shihui.openpf.home.service.api;

import java.util.List;

import com.shihui.openpf.home.model.Goods;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月19日 下午6:42:35
 */
public interface GoodsService {

    String create(Goods goods);

    String update(Goods goods);

    Goods findById(long id);

    Goods findByCity(int categoryId, int cityId);

    List<Goods> list(int categoryId);

    List<Goods> list(int serviceId , int cityId);

    String batchUpdate(List<Goods> goodsList);

}
